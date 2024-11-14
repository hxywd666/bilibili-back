package com.bilibili.pojo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoInfoFilePost {
    private String id;
    private String uploadId;
    private Long userId;
    private String videoId;
    private Integer fileIndex;
    private String fileName;
    private Long fileSize;
    private String filePath;
    private Integer updateType;
    private Integer transferResult;
    private Integer duration;
}

