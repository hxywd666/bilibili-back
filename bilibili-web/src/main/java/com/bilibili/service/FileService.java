package com.bilibili.service;

import com.bilibili.pojo.dto.DeleteUploadedVideo;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadImageDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO);
    void uploadVideo(UploadVideoDTO uploadVideoDTO) throws IOException;
    Result<String> delUploadVideo(DeleteUploadedVideo deleteUploadedVideo) throws IOException;
    public void getImage(HttpServletResponse response, String sourceName);
    Result<String> uploadImage(UploadImageDTO uploadImageDTO) throws IOException;
}
