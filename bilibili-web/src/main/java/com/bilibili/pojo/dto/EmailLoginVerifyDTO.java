package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

//邮箱登录验证DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLoginVerifyDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String captcha;
    @NotBlank
    private String captchaKey;
}
