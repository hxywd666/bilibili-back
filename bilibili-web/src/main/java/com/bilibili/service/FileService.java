package com.bilibili.service;

import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.result.Result;

public interface FileService {
    Result<String> preUploadVideo(PreUploadVideoDTO preUploadVideoDTO);
}
