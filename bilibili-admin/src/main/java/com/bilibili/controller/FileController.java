package com.bilibili.controller;

import com.bilibili.exception.CategoryException;
import com.bilibili.exception.FileErrorException;
import com.bilibili.properties.CategoryProperties;
import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
@RequestMapping("/file")
@Validated
public class FileController {
    // TODO 这里找不到存储平台
    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private CategoryProperties categoryProperties;
    @PostMapping("/uploadImage")
    public Result uploadImage(@NotNull MultipartFile file,@NotNull Boolean createThumbnail){// 获取当前日期并格式化为 yyyyMM 格式
        String currentMonthPath = categoryProperties.getBasePath() + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + "/";

        FileInfo fileInfo = fileStorageService.of(file)
                .setPlatform("aliyun-oss-1") // 确保平台名称与配置一致
                .setPath(currentMonthPath) // 动态设置路径为 "category/yyyyMM/"
                .setObjectId("0")   // 关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") // 关联对象类型，为了方便管理，不需要可以不写
                .thumbnail(true)
                //.putAttr("category", "admin") // 保存一些属性，不需要可以不写
                .upload();  // 将文件上传到对应地方
        if(fileInfo == null){
            throw new FileErrorException("文件上传失败");
        }
        return Result.success(fileInfo.getUrl());
    }

    @GetMapping("/getResource")
    public void getResource(HttpServletResponse response,@NotNull String sourceName){
        System.out.println(sourceName);
    }
}
