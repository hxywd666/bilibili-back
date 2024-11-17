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
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    private CategoryProperties categoryProperties;
    @Autowired
    private OSS ossClient;
    @Autowired
    private FFmpegUtils fFmpegUtils;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO) {
        //随机生成视频上传ID 创建临时上传目录
        Long userId = UserContext.getUserId();
        String uploadId = RandomUtil.randomString(FileConstant.UPLOAD_ID_LENGTH);
        String day = sdf.format(new Date());
        String filePath = day + "/" + userId + uploadId;
        String folder = fileProperties.getVideo() + filePath;
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
    public Result uploadVideo(UploadVideoDTO uploadVideoDTO) throws IOException {
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
        String folder = fileProperties.getVideo() + uploadVideoRedisDTO.getFilePath();
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
        return Result.success();
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
        FileUtils.deleteDirectory(new File(fileProperties.getVideo() + uploadVideoRedisDTO.getFilePath()));
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

            // 创建URL对象，用于解析资源名称
            URL url = new URL(sourceName);
            // 获取主机名，即URL中的域名部分
            String host = url.getHost();

            // 从主机名中提取bucket名称
            String bucketName = host.split("\\.")[0];
            // 从URL路径中提取对象键名，即去除第一个斜杠后的部分
            String objectKey = url.getPath().substring(1);

            // 使用OSS客户端从指定的bucket和对象键名中获取OSS对象
            OSSObject ossObject = ossClient.getObject(bucketName, objectKey);

            // 设置响应内容类型，表示将发送二进制数据
            response.setContentType("application/octet-stream");
            // 提取文件名，即对象键名的最后一部分
            String fileName = objectKey.substring(objectKey.lastIndexOf('/') + 1);
            // 设置响应头，指示浏览器以附件形式下载文件，并指定文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 设置缓存控制头，指示浏览器缓存文件的时间为一个月
            response.setHeader("Cache-Control", "max-age=2592000");

            // 使用try-with-resources语句确保输入流在使用后能被正确关闭
            try (InputStream inputStream = ossObject.getObjectContent()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                // 循环读取输入流中的数据，并写入响应的输出流中
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
            // 强制将响应内容写入客户端
            response.flushBuffer();
        } catch (IOException e) {
            // 当发生IO异常时，抛出自定义异常，指示文件下载失败
            throw new FileErrorException("文件下载失败");
        }
    }  
    @Override
    public Result<String> uploadImage(UploadImageDTO uploadImageDTO) throws IOException {
        String day = sdf.format(new Date());
        String folder = fileProperties.getImage() + FileConstant.COVER_FILE_PATH + day;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            boolean mkdirs = folderFile.mkdirs();
            if (!mkdirs) {
                throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
            }
        }
        String originalFilename = uploadImageDTO.getFile().getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString(true) + fileSuffix;
        String filePath = folder + "/" + fileName;
        uploadImageDTO.getFile().transferTo(new File(filePath));
        if (uploadImageDTO.getCreateThumbnail()) {
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return Result.success(FileConstant.COVER_FILE_PATH + day + "/" + fileName);
    }
}