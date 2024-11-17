package com.bilibili.controller;

import com.bilibili.pojo.dto.CategoryGetFileSourceNameDTO;
import com.bilibili.pojo.dto.DeleteUploadedVideo;
import com.bilibili.pojo.dto.PreUploadVideoDTO;
import com.bilibili.pojo.dto.UploadImageDTO;
import com.bilibili.pojo.dto.UploadVideoDTO;
import com.bilibili.properties.SysSettingProperties;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
        fileService.uploadVideo(uploadVideoDTO);
        return Result.success();
    }

    //删除上传文件
    @PostMapping("/delUploadVideo")
    public Result<String> delUploadVideo(@Valid DeleteUploadedVideo deleteUploadedVideo) throws IOException {
        return fileService.delUploadVideo(deleteUploadedVideo);
    }

    //读取系统设置
    @GetMapping("/getSetting")
    public Result<SysSettingProperties> getSetting() {
        return Result.success(new SysSettingProperties());
    }

    @GetMapping("/getResource")
    public void getResource(HttpServletResponse response, CategoryGetFileSourceNameDTO categoryGetFileSourceNameDTO) {
        fileService.getImage(response,categoryGetFileSourceNameDTO.getSourceName());
    }
  
    //上传图片接口
    @PostMapping("/uploadImage")
    public Result<String> uploadImage(@Valid UploadImageDTO uploadImageDTO) throws IOException {
        return fileService.uploadImage(uploadImageDTO);
    }
}
