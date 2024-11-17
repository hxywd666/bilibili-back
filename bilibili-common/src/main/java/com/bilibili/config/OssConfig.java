package com.bilibili.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Value("${dromara.x-file-storage.aliyun-oss[0].end-point}")
    private String endpoint;

    @Value("${dromara.x-file-storage.aliyun-oss[0].access-key}")
    private String accessKeyId;

    @Value("${dromara.x-file-storage.aliyun-oss[0].secret-key}")
    private String accessKeySecret;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}