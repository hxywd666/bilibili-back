package com.bilibili.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "system.setting")
public class SysSettingProperties {
    private Integer registerCoinCount = 10; //注册赠送的硬币数量
    private Integer postVideoCoinCount = 10; //发布视频赠送的硬币数量
    private Integer videoSize = 10 * 1024 * 1024; //视频大小限制(bytes)
    private Integer videoPCount = 10; //视频最大分P数量
    private Integer videoCount = 10;
    private Integer commentCount = 20;
    private Integer danmuCount = 20;
}