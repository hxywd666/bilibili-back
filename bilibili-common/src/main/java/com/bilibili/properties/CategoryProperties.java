package com.bilibili.properties;


import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dromara.x-file-storage.aliyun-oss")
@Data
public class CategoryProperties {
    private String basePath;
}
