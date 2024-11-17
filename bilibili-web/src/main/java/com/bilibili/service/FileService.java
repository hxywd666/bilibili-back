package com.bilibili.service;

import com.bilibili.pojo.dto.DeleteUploadedVideo;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO);
    Result uploadVideo(UploadVideoDTO uploadVideoDTO) throws IOException;

    Result<String> delUploadVideo(DeleteUploadedVideo deleteUploadedVideo) throws IOException;

    // 种类获取图片
    public void getImage(HttpServletResponse response, String sourceName);

}
