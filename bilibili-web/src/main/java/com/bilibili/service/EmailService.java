package com.bilibili.service;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.EmailSendDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.EmailSendVO;
import com.bilibili.result.Result;

public interface EmailService {
    Result<EmailSendVO> emailSend(EmailSendDTO emailSendDTO);

    Result<EmailLoginVerifyVO> emailVerifyLogin(EmailLoginVerifyDTO emailLoginVerifyDTO);
}
