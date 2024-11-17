package com.bilibili.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileService {

    public String uploadImage(MultipartFile file,Boolean createThumbnail) ;

    public void getImage(HttpServletResponse response, String sourceName);
}
