package com.bilibili.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.bilibili.constant.AccountConstant;
import com.bilibili.constant.EmailConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.EmailErrorException;
import com.bilibili.exception.ParamErrorException;
import com.bilibili.pojo.dto.EmailSendDTO;
import com.bilibili.pojo.vo.EmailSendVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import com.bilibili.utils.EmailUtils;
import com.bilibili.utils.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<EmailSendVO> emailSend(EmailSendDTO emailSendDTO) {
        // 校验参数
        String email = emailSendDTO.getEmail();
        // 校验验证码
        if (!emailSendDTO.getCheckCode().equals(redisTemplate.opsForValue().get(emailSendDTO.getCheckCodeKey()))) {
            redisTemplate.delete(emailSendDTO.getCheckCodeKey());
            throw new EmailErrorException(MessageConstant.CAPTCHA_ERROR);
        }
        // 生成验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        // 异步发送邮件
        sendEmailAsync(email, verificationCode);
        // 生成key
        String captchaKey = RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.EMAIL_REDIS_KEY
                + RedisConstant.CAPTCHA_REDIS_KEY
                + UUID.randomUUID(true);
        // 存入 redis
        redisTemplate.opsForValue().set(captchaKey, verificationCode,
                RedisConstant.EMAIL_CAPTCHA_EXPIRE, TimeUnit.MILLISECONDS);
        EmailSendVO vo = new EmailSendVO();
        vo.setCaptchaKey(captchaKey);
        return Result.success(vo);
    }

    public void sendEmailAsync(String email, String verificationCode) {
        try {
            emailUtils.sendingEmail(email, EmailConstant.EMAIL_LOGIN_TITLE, verificationCode);
        } catch (Exception e) {
            log.error("发送邮件失败:{}", e.getMessage());
        }
    }
}