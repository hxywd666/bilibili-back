package com.bilibili.utils;

import com.bilibili.constant.FileConstant;
import com.bilibili.properties.AdminProperties;
import com.bilibili.properties.FFmpegProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FFmpegUtils {

    @Resource
    private AdminProperties adminProperties;

    @Resource
    private FFmpegProperties fFmpegProperties;

    public void createImageThumbnail(String filePath){
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        CMD = String.format(CMD,filePath,filePath + FileConstant.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(CMD,fFmpegProperties.getShowFFmpegLog());
    }

}
