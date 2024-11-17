package com.bilibili.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

public interface CategoryFileService {

    public String uploadImage(MultipartFile file,Boolean createThumbnail) ;

    public void getImage(HttpServletResponse response, String sourceName);
}
