package com.bilibili.pojo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class UploadVideoDTO {
    private MultipartFile chunkFile;
    private Integer chunkIndex;
    private String uploadId;
}
