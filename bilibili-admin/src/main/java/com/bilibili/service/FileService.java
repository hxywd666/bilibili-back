package com.bilibili.service;

import com.bilibili.pojo.dto.UploadImageDTO;
import com.bilibili.result.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileService {

    public Result<String> uploadImage(UploadImageDTO uploadImageDTO) ;

    public void getImage(HttpServletResponse response, String sourceName);
}
