package com.bilibili.controller;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.EmailSendDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.EmailSendVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/email")
@Validated
public class EmailController {

    @Autowired
    private EmailService emailService;

    //发送邮箱验证码接口
    @PostMapping("/captcha")
    public Result<EmailSendVO> emailSend(@Valid @RequestBody EmailSendDTO emailSendDTO) {
        return emailService.emailSend(emailSendDTO);
    }
}
