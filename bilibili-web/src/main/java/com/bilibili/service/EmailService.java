package com.bilibili.service;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.result.Result;

public interface EmailService {
    Result<EmailLoginVerifyVO> emailLoginVerify(EmailLoginVerifyDTO emailLoginVerifyDTO);
}
