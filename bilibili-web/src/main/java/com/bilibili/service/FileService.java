package com.bilibili.service;

import com.bilibili.pojo.dto.DeleteUploadedVideo;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadImageDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.result.Result;

import java.io.IOException;

public interface FileService {
    Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO);
    Result uploadVideo(UploadVideoDTO uploadVideoDTO) throws IOException;

    Result<String> delUploadVideo(DeleteUploadedVideo deleteUploadedVideo) throws IOException;

    Result<String> uploadImage(UploadImageDTO uploadImageDTO) throws IOException;
}
