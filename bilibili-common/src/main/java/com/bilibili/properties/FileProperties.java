package com.bilibili.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FileProperties {
    @Value("${file-folder.local.video}")
    private String videoLocalTempFolder; //本地存储上传视频临时文件的目录
    @Value("${file-folder.oss.video}")
    private String videoOssFolder; //视频文件在OSS上的目录前缀
    @Value("${file-folder.oss.image}")
    private String imageOssFolder; //图片文件在OSS上的目录前缀
}