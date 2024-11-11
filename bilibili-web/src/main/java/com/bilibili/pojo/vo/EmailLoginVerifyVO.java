package com.bilibili.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailLoginVerifyVO {
    private String token;
    private String captcha;
}
