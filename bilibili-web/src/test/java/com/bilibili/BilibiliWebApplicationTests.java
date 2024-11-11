package com.bilibili;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
class BilibiliWebApplicationTests {

    @Autowired
    private JavaMailSender sender;

    private String subject = "邮件标题";
    private String content = "邮件正文";
    private String to = "codingfish0706@126.com";
    private String from = to;

    @Test
    void contextLoads() throws MessagingException, IOException {
        String htmlContent = generateHtmlContent("4906");
        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(from);
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
