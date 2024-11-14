package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank
    private String account;
    @NotBlank
    private String password;
    @NotBlank
    private String checkCodeKey;
    @NotBlank
    private String checkCode;
}
