package com.bilibili.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfoFile {
    private String id;
    private Long userId;
    private String videoId;
    private String fileName;
    private Integer fileIndex;
    private Long fileSize;
    private String filePath;
    private Integer duration;
}

