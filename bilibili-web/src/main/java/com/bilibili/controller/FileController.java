package com.bilibili.controller;

import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Validated
public class FileController {
    @Autowired
    private FileService fileService;

    //文件预上传接口
    @PostMapping("/preUploadVideo")
    public Result<String> preUploadVideo(@Valid PreUploadVideoDTO preUploadVideoDTO) {
        return fileService.preUploadVideo(preUploadVideoDTO);
    }

    //文件正式上传
    @PostMapping("/uploadVideo")
    public Result uploadVideo(@Valid UploadVideoDTO uploadVideoDTO) throws IOException {
        return fileService.uploadVideo(uploadVideoDTO);
    }

}
