package com.bilibili;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

@SpringBootTest
class BilibiliWebApplicationTests {

    @Autowired
    private JavaMailSender sender;

    private String subject = "邮件标题";
    private String content = "邮件正文";
    private String to = "linghuadegou8@gmail.com";
    private String from = "codingfish0706@126.com";

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

    private JavaMailSender createMailSender(String host, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 设置邮件服务器的主机
        mailSender.setHost(host);
        // 设置邮件服务器的端口
        mailSender.setPort(587);

        // 设置邮件服务器的用户名和密码
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // 设置邮件服务器的其他属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // 开启调试模式，方便查看日志

        return mailSender;
    }
}