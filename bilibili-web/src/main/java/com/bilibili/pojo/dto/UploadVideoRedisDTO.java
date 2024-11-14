package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadVideoRedisDTO {
    private String uploadId;
    private String fileName;
    private Integer chunkIndex;
    private Integer chunks;
    private Long fileSize = 0L;
    private String filePath;
}
