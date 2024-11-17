package com.bilibili.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.bilibili.constant.FileConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.exception.FileErrorException;
import com.bilibili.pojo.dto.*;
import com.bilibili.properties.CategoryProperties;
import com.bilibili.properties.FileProperties;
import com.bilibili.properties.SysSettingProperties;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import com.bilibili.utils.ConvertUtils;
import com.bilibili.utils.FFmpegUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private SysSettingProperties sysSettingProperties;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private OSS ossClient;
    @Autowired
    private FFmpegUtils fFmpegUtils;


    @Override
    public Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO) {
        //随机生成视频上传ID 创建临时上传目录
        Long userId = UserContext.getUserId();
        String uploadId = RandomUtil.randomString(FileConstant.UPLOAD_ID_LENGTH);
        String day = LocalDate.now().format(DateTimeFormatter.ofPattern(FileConstant.FILE_NAME_DATE_FORMAT));
        String filePath = day + "/" + userId + uploadId;
        String folder = fileProperties.getVideoLocalTempFolder() + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            boolean mkdirs = folderFile.mkdirs();
            if (!mkdirs) {
                throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
            }
        }
        UploadVideoRedisDTO uploadVideoRedisDTO = UploadVideoRedisDTO.builder()
                .uploadId(uploadId)
                .fileName(preUploadVideoDTO.getFileName())
                .chunkIndex(0)
                .chunks(preUploadVideoDTO.getChunks())
                .filePath(filePath)
                .build();
        redisTemplate.opsForValue().set(
                RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.FILE_REDIS_KEY
                + RedisConstant.UPLOAD_FILE_REDIS_KEY
                + userId + ":"
                + uploadId ,
                uploadVideoRedisDTO,
                RedisConstant.UPLOAD_FILE_EXPIRE,
                TimeUnit.MILLISECONDS
        );
        return Result.success(uploadId);
    }

    @Override
    public void uploadVideo(UploadVideoDTO uploadVideoDTO) throws IOException {
        Integer currentChunkIndex = uploadVideoDTO.getChunkIndex();
        //获取预上传文件信息
        Object object = redisTemplate.opsForValue().get(
                RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.FILE_REDIS_KEY
                + RedisConstant.UPLOAD_FILE_REDIS_KEY
                + UserContext.getUserId() + ":"
                + uploadVideoDTO.getUploadId()
        );
        UploadVideoRedisDTO uploadVideoRedisDTO = ConvertUtils.convertObject(object, UploadVideoRedisDTO.class);
        //如果没有预上传信息直接报错
        if (uploadVideoRedisDTO == null) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        Long uploadedFileSize = uploadVideoRedisDTO.getFileSize();
        //校验已经上传的文件大小
        if (uploadedFileSize > sysSettingProperties.getVideoSize()) {
            throw new FileErrorException(MessageConstant.FILE_OVERSIZE);
        }
        //判断分片索引是否正确
        if ((currentChunkIndex - 1) > uploadVideoRedisDTO.getChunkIndex()
                || currentChunkIndex > uploadVideoRedisDTO.getChunks() - 1) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        String folder = fileProperties.getVideoLocalTempFolder() + uploadVideoRedisDTO.getFilePath();
        File targetFile = new File(folder + "/" + currentChunkIndex);
        uploadVideoDTO.getChunkFile().transferTo(targetFile);
        uploadVideoRedisDTO.setChunkIndex(currentChunkIndex);
        uploadVideoRedisDTO.setFileSize(uploadedFileSize + uploadVideoDTO.getChunkFile().getSize());
        redisTemplate.opsForValue().set(
                RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.FILE_REDIS_KEY
                + RedisConstant.UPLOAD_FILE_REDIS_KEY
                + UserContext.getUserId() + ":"
                + uploadVideoDTO.getUploadId(),
                uploadVideoRedisDTO,
                RedisConstant.UPLOAD_FILE_EXPIRE,
                TimeUnit.MICROSECONDS
        );
    }

    @Override
    public Result<String> delUploadVideo(DeleteUploadedVideo deleteUploadedVideo) throws IOException {
        String uploadId = deleteUploadedVideo.getUploadId();
        //获取预上传文件信息
        Object object = redisTemplate.opsForValue().get(
                RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.FILE_REDIS_KEY
                + RedisConstant.UPLOAD_FILE_REDIS_KEY
                + UserContext.getUserId() + ":"
                + uploadId
        );
        UploadVideoRedisDTO uploadVideoRedisDTO = ConvertUtils.convertObject(object, UploadVideoRedisDTO.class);
        if (uploadVideoRedisDTO == null) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        redisTemplate.delete(
                RedisConstant.CLIENT_KEY_PREFIX
                     + RedisConstant.FILE_REDIS_KEY
                     + RedisConstant.UPLOAD_FILE_REDIS_KEY
                     + UserContext.getUserId() + ":"
                     + uploadId
        );
        FileUtils.deleteDirectory(new File(fileProperties.getVideoLocalTempFolder()+ uploadVideoRedisDTO.getFilePath()));
        return Result.success(uploadId);
    }
    /**
     * 种类获取
     * 根据资源名称获取资源文件,
     * 该方法主要用于下载存储在OSS（对象存储服务）中的文件
     * 它通过解析资源名称来确定文件的位置，并将文件内容写入HTTP响应中
     *
     * @param response HTTP响应对象，用于输出文件内容
     * @param sourceName 资源名称，即文件的URL路径
     * @throws FileErrorException 当文件下载失败时抛出此异常
     */
    @Override
    public void getImage(HttpServletResponse response, String sourceName) {
        try {
            URL url = new URL(sourceName);
            String host = url.getHost();
            String bucketName = host.split("\\.")[0];
            String objectKey = url.getPath().substring(1);
            OSSObject ossObject = ossClient.getObject(bucketName, objectKey);
            response.setContentType("application/octet-stream");
            String fileName = objectKey.substring(objectKey.lastIndexOf('/') + 1);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Cache-Control", "max-age=2592000");
            try (InputStream inputStream = ossObject.getObjectContent()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            throw new FileErrorException(MessageConstant.FILE_DOWNLOAD_ERROR);
        }
    }  
    @Override
    public Result<String> uploadImage(UploadImageDTO uploadImageDTO) throws IOException {
        MultipartFile file = uploadImageDTO.getFile();
        Boolean createThumbnail = uploadImageDTO.getCreateThumbnail();
        String filePath = fileProperties.getImageOssFolder() + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/";
        String fileName = UUID.randomUUID().toString(true);
        FileInfo fileInfo = fileStorageService.of(file)
                .setName(fileName)
                .setPlatform("aliyun-oss-1")
                .setPath(filePath)
                .setObjectId("0")
                .setObjectType("0")
                .thumbnail(createThumbnail)
                .upload();
        if(fileInfo == null){
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        return Result.success(fileInfo.getUrl());
    }
}