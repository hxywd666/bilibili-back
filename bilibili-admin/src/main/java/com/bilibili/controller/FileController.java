package com.bilibili.controller;

import com.bilibili.pojo.dto.CategoryGetFileSourceNameDTO;
import com.bilibili.pojo.dto.CategoryUploadFileDTO;
import com.bilibili.result.Result;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/file")
@Validated
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/uploadImage")
    public Result uploadImage(CategoryUploadFileDTO categoryUploadFileDTO){// 获取当前日期并格式化为 yyyyMM 格式
        String url = fileService.uploadImage(categoryUploadFileDTO.getFile(), categoryUploadFileDTO.getCreateThumbnail());
        // 返回日期和名称组合的字符串
        return Result.success(url);
    }

    @GetMapping("/getResource")
    public void getResource(HttpServletResponse response, CategoryGetFileSourceNameDTO categoryGetFileSourceNameDTO) {
        fileService.getImage(response,categoryGetFileSourceNameDTO.getSourceName());
    }
}
