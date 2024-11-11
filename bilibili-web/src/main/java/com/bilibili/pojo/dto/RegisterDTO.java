package com.bilibili.pojo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String nickName;
    private String registerPassword;
    private String checkCodeKey;
    private String checkCode;
}
