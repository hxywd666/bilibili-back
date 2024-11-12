package com.bilibili.pojo.dto;

import lombok.Data;

//邮箱登录验证DTO
@Data
public class EmailLoginVerifyDTO {
    private String email;
    private String captcha;
    private String captchaKey;
}
