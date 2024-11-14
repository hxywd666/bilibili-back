package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadVideoDTO {
    @NotNull
    private MultipartFile chunkFile;
    @NotNull
    private Integer chunkIndex;
    @NotBlank
    private String uploadId;
}
