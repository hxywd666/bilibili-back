package com.bilibili.controller;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public Result<EmailLoginVerifyVO> emailLoginVerify(@RequestBody EmailLoginVerifyDTO emailLoginVerifyDTO) {
        return emailService.emailLoginVerify(emailLoginVerifyDTO);
    }


}
