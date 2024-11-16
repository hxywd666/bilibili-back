package com.bilibili.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FFmpegProperties {

    @Value("${project.folder}")
    private String projectFolder;

    @Value("${showFFmpegLog:true}")
    private Boolean showFFmpegLog;

}
