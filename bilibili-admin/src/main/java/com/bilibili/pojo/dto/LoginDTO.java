package com.bilibili.pojo.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String account;
    private String password;
    private String checkCodeKey;
    private String checkCode;
}
