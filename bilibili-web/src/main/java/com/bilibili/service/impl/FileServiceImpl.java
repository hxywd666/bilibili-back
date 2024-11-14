package com.bilibili.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.bilibili.constant.FileConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.PreUploadVideoRedisDTO;
import com.bilibili.properties.FileProperties;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FileProperties fileProperties;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO) {
        //随机生成视频上传ID 创建临时上传文件夹
        String uploadId = RandomUtil.randomString(FileConstant.UPLOAD_ID_LENGTH);
        String day = sdf.format(new Date());
        String filePath = day + "/" + UserContext.getUserId() + uploadId;
        String folder = fileProperties.getVideo() + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        PreUploadVideoRedisDTO preUploadVideoRedisDTO = PreUploadVideoRedisDTO.builder()
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
                + UserContext.getUserId()
                + ":" + uploadId ,
                preUploadVideoRedisDTO,
                RedisConstant.UPLOAD_FILE_EXPIRE,
                TimeUnit.MICROSECONDS
        );
        return Result.success(uploadId);
    }
}