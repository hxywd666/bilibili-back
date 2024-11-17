package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUploadFileDTO {

    @NotNull
    private MultipartFile file;

    @NotNull
    private Boolean createThumbnail;
}
