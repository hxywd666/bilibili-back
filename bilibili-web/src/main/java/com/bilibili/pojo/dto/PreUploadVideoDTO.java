package com.bilibili.pojo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreUploadVideoDTO {

    @NotBlank
    private String fileName;

    @NotNull
    private Integer chunks;
}
