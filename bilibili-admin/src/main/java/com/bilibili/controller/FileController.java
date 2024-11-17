package com.bilibili.controller;

import com.bilibili.pojo.dto.CategoryGetFileSourceNameDTO;
import com.bilibili.pojo.dto.UploadImageDTO;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
@Validated
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/uploadImage")
    public Result<String> uploadImage(UploadImageDTO uploadImageDTO){
        return fileService.uploadImage(uploadImageDTO);
    }

    @GetMapping("/getResource")
    public void getResource(HttpServletResponse response, CategoryGetFileSourceNameDTO categoryGetFileSourceNameDTO) {
        fileService.getImage(response,categoryGetFileSourceNameDTO.getSourceName());
    }
}
