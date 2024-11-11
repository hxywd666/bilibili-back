package com.bilibili.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckCodeVO {
    private String checkCode; //验证码图片base64编码
    private String checkCodeKey; //验证码图片在Redis中的key
}
