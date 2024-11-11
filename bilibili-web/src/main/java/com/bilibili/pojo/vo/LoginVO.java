package com.bilibili.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class LoginVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9170480547933408839L;
    private String userId;
    private String nickName;
    private String avatar;
    private Long expireAt;
    private String token;
    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}
