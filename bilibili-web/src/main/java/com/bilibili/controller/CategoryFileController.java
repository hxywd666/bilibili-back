package com.bilibili.controller;

import com.bilibili.result.Result;
import com.bilibili.service.CategoryFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@RequestMapping("/file")
@Validated
public class CategoryFileController {

    @Resource
    private CategoryFileService categoryFileService;


    @GetMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) {
        categoryFileService.getImage(response,sourceName);
    }


}