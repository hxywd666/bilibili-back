package com.bilibili.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//邮箱发送返回
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendVO {
    private String captchaKey;
}
