package com.bilibili.utils;

import com.bilibili.constant.FileConstant;
import org.springframework.stereotype.Component;

@Component
public class FFmpegUtils {

    public void createImageThumbnail(String filePath){
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        CMD = String.format(CMD,filePath,filePath + FileConstant.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(CMD,true);
    }

}