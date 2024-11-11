package com.bilibili.service.impl;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public Result<EmailLoginVerifyVO> emailLoginVerify(EmailLoginVerifyDTO emailLoginVerifyDTO) {

        return null;
    }
}
