package com.bilibili.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.bilibili.exception.FileErrorException;
import com.bilibili.properties.CategoryProperties;
import com.bilibili.service.CategoryFileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class CategoryFileServiceImpl implements CategoryFileService {
    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private CategoryProperties categoryProperties;

    @Resource
    private OSSClient ossClient;
    /**
     * 根据资源名称获取资源文件
     * 该方法主要用于下载存储在OSS（对象存储服务）中的文件
     * 它通过解析资源名称来确定文件的位置，并将文件内容写入HTTP响应中
     *
     * @param response HTTP响应对象，用于输出文件内容
     * @param sourceName 资源名称，即文件的URL路径
     * @throws FileErrorException 当文件下载失败时抛出此异常
     */
    @Override
    public void getImage(HttpServletResponse response, String sourceName) {
        try {

            // 创建URL对象，用于解析资源名称
            URL url = new URL(sourceName);
            // 获取主机名，即URL中的域名部分
            String host = url.getHost();

            // 从主机名中提取bucket名称
            String bucketName = host.split("\\.")[0];
            // 从URL路径中提取对象键名，即去除第一个斜杠后的部分
            String objectKey = url.getPath().substring(1);

            // 使用OSS客户端从指定的bucket和对象键名中获取OSS对象
            OSSObject ossObject = ossClient.getObject(bucketName, objectKey);

            // 设置响应内容类型，表示将发送二进制数据
            response.setContentType("application/octet-stream");
            // 提取文件名，即对象键名的最后一部分
            String fileName = objectKey.substring(objectKey.lastIndexOf('/') + 1);
            // 设置响应头，指示浏览器以附件形式下载文件，并指定文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 设置缓存控制头，指示浏览器缓存文件的时间为一个月
            response.setHeader("Cache-Control", "max-age=2592000");

            // 使用try-with-resources语句确保输入流在使用后能被正确关闭
            try (InputStream inputStream = ossObject.getObjectContent()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                // 循环读取输入流中的数据，并写入响应的输出流中
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
            // 强制将响应内容写入客户端
            response.flushBuffer();
        } catch (IOException e) {
            // 当发生IO异常时，抛出自定义异常，指示文件下载失败
            throw new FileErrorException("File download failed");
        }
    }
}
