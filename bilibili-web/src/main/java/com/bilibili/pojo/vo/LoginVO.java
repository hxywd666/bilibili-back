package com.bilibili.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {
    private String userId;
    private String nickName;
    private String avatar;
    private Long expireAt;
    private String token;
    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}
