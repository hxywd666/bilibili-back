package com.bilibili.utils;

import com.bilibili.properties.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private MailProperties mailProperties;

    // 发送邮件
    @Async
    public void sendingEmail(String to, String subject, String verificationCode) throws MessagingException, IOException {
        String htmlContent = generateHtmlContent(verificationCode);
        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        sender.send(mail);
    }

    private String generateHtmlContent(String verificationCode) throws IOException {
        // 读取资源文件夹中的HTML模板文件
        ClassPathResource resource = new ClassPathResource("emailTemplate.html");
        String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));

        // 替换占位符
        return htmlTemplate.replace("${verificationCode}", verificationCode);
    }

}
