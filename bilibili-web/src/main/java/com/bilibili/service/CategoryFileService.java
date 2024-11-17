package com.bilibili.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface CategoryFileService {

    public void getImage(HttpServletResponse response, String sourceName);
}
