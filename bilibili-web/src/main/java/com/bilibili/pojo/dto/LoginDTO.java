package com.bilibili.pojo.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
    private String checkCode;
    private String checkCodeKey;
}
