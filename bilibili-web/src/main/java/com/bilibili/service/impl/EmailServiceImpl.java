package com.bilibili.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.AccountConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.enumeration.AccountGenderEnum;
import com.bilibili.enumeration.AccountStatusEnum;
import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.CaptchaErrorException;
import com.bilibili.exception.EmailErrorException;
import com.bilibili.mapper.AccountMapper;
import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.EmailSendDTO;
import com.bilibili.pojo.entity.User;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.EmailSendVO;
import com.bilibili.result.Result;
import com.bilibili.service.EmailService;
import com.bilibili.utils.EmailUtils;
import com.bilibili.utils.GetIpUtils;
import com.bilibili.utils.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailServiceImpl extends ServiceImpl<AccountMapper, User> implements EmailService {

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GetIpUtils getIpUtils;

    @Override
    public Result<EmailSendVO> emailSend(EmailSendDTO emailSendDTO) {
        // 获取邮箱并校验
        String email = emailSendDTO.getEmail();
        if (!ValidatorUtils.isValidEmail(email))
            throw new EmailErrorException(MessageConstant.EMAIL_FORMAT_ERROR);
        // 生成验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        // 发送邮件
        try {
            emailUtils.sendingEmail(email, "Bilibili邮箱验证码", verificationCode);
        } catch (Exception e) {
            log.error("发送邮件失败，{}", e.getMessage());
            return Result.error(HttpStatusEnum.INTERNAL_SERVER_ERROR,
                    MessageConstant.EMAIL_SENDING_ERROR);
        }
        // 生成key
        String captchaKey = AccountConstant.CLIENT_KEY_PREFIX
                + AccountConstant.EMAIL_CAPTCHA_REDIS_KEY
                + UUID.randomUUID(true);
        // 存入 redis
        redisTemplate.opsForValue().set(captchaKey, verificationCode,
                AccountConstant.CAPTCHA_EXPIRE, TimeUnit.MILLISECONDS);
        EmailSendVO vo = new EmailSendVO();
        vo.setCaptchaKey(captchaKey);
        return Result.success(vo);
    }

    @Override
    public Result<EmailLoginVerifyVO> emailVerifyLogin(HttpServletRequest request, HttpServletResponse response,
                                                       EmailLoginVerifyDTO emailLoginVerifyDTO) {
        // 判断邮箱验证码是否正确
        String captchaKey = emailLoginVerifyDTO.getCaptchaKey();
        String captcha = (String) redisTemplate.opsForValue()
                .get(captchaKey);
        // 删除验证码
        redisTemplate.delete(captchaKey);
        // 不正确
        if (captcha == null || !captcha.equals(emailLoginVerifyDTO.getCaptcha())) {
            throw new CaptchaErrorException(MessageConstant.CAPTCHA_ERROR);
        }
        // 正确
        String email = emailLoginVerifyDTO.getEmail();
        // 判断用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        // 不存在，注册用户
        if (count(queryWrapper) == 0) {
            User user = User.builder()
                    .email(emailLoginVerifyDTO.getEmail())
                    // TODO 先用邮箱替代
                    .nickName(email)
                    .sex(AccountGenderEnum.UNKNOWN.getCode())
                    .registerTime(new Date())
                    .status(AccountStatusEnum.NORMAL.getCode())
                    .totalCoinCount(AccountConstant.DEFAULT_COIN_COUNT)
                    .currentCoinCount(AccountConstant.DEFAULT_COIN_COUNT)
                    .theme(AccountConstant.DEFAULT_THEME)
                    .build();
            save(user);
        }
        // 更新用户登录信息
        User userInfo = User.builder()
                .lastLoginTime(new Date())
                .lastLoginIp(getIpUtils.getIpAddress(request))
                .email(email)
                .build();
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("email", email);
        // 根据实体类更新
        update(userInfo, wrapper);
        // 查询用户信息
        User user = getOne(queryWrapper);
        //生成token 存入Redis、Cookie
        String token = UUID.randomUUID(true).toString();
        // 准备响应实体类
        EmailLoginVerifyVO emailLoginVerifyVO = EmailLoginVerifyVO.builder()
                .userId(String.valueOf(user.getId()))
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .token(token)
                .expireAt(System.currentTimeMillis() + AccountConstant.TOKEN_EXPIRE)
                .build();
        redisTemplate.opsForValue().set(
                AccountConstant.CLIENT_KEY_PREFIX + AccountConstant.LOGIN_REDIS_KEY + token,
                emailLoginVerifyVO,
                AccountConstant.TOKEN_EXPIRE,
                TimeUnit.MILLISECONDS
        );

        //删除上一次的token(非必要)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                    redisTemplate.delete(AccountConstant.CLIENT_KEY_PREFIX + AccountConstant.LOGIN_REDIS_KEY + cookie.getValue());
                    break;
                }
            }
        }

        //将新token存入Cookie返回
        Cookie cookie = new Cookie(AccountConstant.CLIENT_COOKIE_KEY, token);
        cookie.setMaxAge(AccountConstant.TOKEN_EXPIRE / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);

        return Result.success(emailLoginVerifyVO);
    }
}
