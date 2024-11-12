package com.bilibili.service.impl;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.EmailSendDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.EmailSendVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public Result<EmailSendVO> emailSend(EmailSendDTO emailSendDTO) {

        return null;
    }

    @Override
    public Result<EmailLoginVerifyVO> emailVerifyLogin(EmailLoginVerifyDTO emailLoginVerifyDTO) {

        return null;
    }
}
