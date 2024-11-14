package com.bilibili.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.bilibili.constant.FileConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.constant.SystemConstant;
import com.bilibili.context.UserContext;
import com.bilibili.exception.FileErrorException;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadVideoRedisDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.properties.FileProperties;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import com.bilibili.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO) {
        //随机生成视频上传ID 创建临时上传目录
        Long userId = UserContext.getUserId();
        log.error("{}", userId);
        String uploadId = RandomUtil.randomString(FileConstant.UPLOAD_ID_LENGTH);
        String day = sdf.format(new Date());
        String filePath = day + "/" + userId + uploadId;
        String folder = fileProperties.getVideo() + FileConstant.TEMP_VIDEO_UPLOAD_FOLDER + filePath;
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
        log.error("{}", uploadVideoRedisDTO);
        //如果没有预上传信息直接报错
        if (uploadVideoRedisDTO == null) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        Long uploadedFileSize = uploadVideoRedisDTO.getFileSize();
        //校验已经上传的文件大小
        if (uploadedFileSize > SystemConstant.VIDEO_FILE_MAX_SIZE) {
            throw new FileErrorException(MessageConstant.FILE_OVERSIZE);
        }
        //判断分片索引是否正确
        if ((currentChunkIndex - 1) > uploadVideoRedisDTO.getChunkIndex()
                || currentChunkIndex > uploadVideoRedisDTO.getChunks() - 1) {
            throw new FileErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        String folder = fileProperties.getVideo() + FileConstant.TEMP_VIDEO_UPLOAD_FOLDER + uploadVideoRedisDTO.getFilePath();
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
}