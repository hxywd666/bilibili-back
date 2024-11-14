package com.bilibili.pojo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class PreUploadVideoRedisDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8442729330848992831L;
    private String uploadId;
    private String fileName;
    private Integer chunkIndex;
    private Integer chunks;
    private Long fileSize = 0L;
    private String filePath;
}
