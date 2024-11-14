package com.bilibili.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLoginVerifyVO {
    private String userId;
    private String nickName;
    private String avatar;
    private Long expireAt;
    private String token;
    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}
