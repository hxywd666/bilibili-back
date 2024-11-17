package com.bilibili.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.AccountConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.enumeration.AccountGenderEnum;
import com.bilibili.enumeration.AccountStatusEnum;
import com.bilibili.exception.LoginErrorException;
import com.bilibili.mapper.AccountMapper;
import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.entity.User;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.LoginVO;
import com.bilibili.properties.SysSettingProperties;
import com.bilibili.result.Result;
import com.bilibili.service.AccountService;
import com.bilibili.utils.ConvertUtils;
import com.bilibili.utils.IpUtils;
import com.wf.captcha.ArithmeticCaptcha;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, User> implements AccountService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SysSettingProperties sysSettingProperties;
    
    @Override
    public Result<CheckCodeVO> getCheckCode() {
        //获取验证码答案和图片 并存入Redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(AccountConstant.CAPTCHA_WIDTH, AccountConstant.CAPTCHA_HEIGHT);
        String captchaCode = captcha.text();
        String captchaImage = captcha.toBase64();
        String redisKey = RedisConstant.CLIENT_KEY_PREFIX
                + RedisConstant.CAPTCHA_REDIS_KEY
                + UUID.randomUUID(true);
        redisTemplate.opsForValue().set(redisKey, captchaCode, RedisConstant.PIC_CAPTCHA_EXPIRE, TimeUnit.MILLISECONDS);
        CheckCodeVO checkCodeVO = CheckCodeVO.builder()
                .checkCode(captchaImage)
                .checkCodeKey(redisKey)
                .build();
        return Result.success(checkCodeVO);
    }

    @Override
    public Result<LoginVO> login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) {
        //检查验证码并删除
        String captcha = (String) redisTemplate.opsForValue().get(loginDTO.getCheckCodeKey());
        if (captcha == null || !captcha.equalsIgnoreCase(loginDTO.getCheckCode())) {
            redisTemplate.delete(loginDTO.getCheckCodeKey());
            throw new LoginErrorException(MessageConstant.CAPTCHA_ERROR);
        }
        redisTemplate.delete(loginDTO.getCheckCodeKey());

        //检查邮箱、密码(前端已经加密过了)、账户状态
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, loginDTO.getEmail());
        User user = getOne(queryWrapper);
        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            throw new LoginErrorException(MessageConstant.EMAIL_OR_PASSWORD_ERROR);
        }
        if (user.getStatus() != AccountStatusEnum.NORMAL.getCode()) {
            throw new LoginErrorException(MessageConstant.USER_STATUS_ERROR);
        }

        //更新用户登录信息
        User userInfo = User.builder()
                .lastLoginTime(new Date())
                .lastLoginIp(IpUtils.getIpAddress(request))
                .id(user.getId())
                .build();
        updateById(userInfo);

        //生成token 存入Redis、Cookie
        String token = UUID.randomUUID(true).toString();
        LoginVO loginVO = LoginVO.builder()
                .userId(String.valueOf(user.getId()))
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .token(token)
                .expireAt(System.currentTimeMillis() + RedisConstant.LOGIN_TOKEN_EXPIRE)
                .build();
        redisTemplate.opsForValue().set(
                RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + token,
                loginVO,
                RedisConstant.LOGIN_TOKEN_EXPIRE,
                TimeUnit.MILLISECONDS
        );

        //删除上一次的token(非必要)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                    redisTemplate.delete(RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + cookie.getValue());
                    break;
                }
            }
        }

        //将新token存入Cookie返回
        Cookie cookie = new Cookie(AccountConstant.CLIENT_COOKIE_KEY, token);
        cookie.setMaxAge(AccountConstant.COOKIE_LOGIN_TOKEN_EXPIRE / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);

        return Result.success(loginVO);
    }

    @Override
    public Result<LoginVO> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        //去Redis检查登录是否过期
        String token = request.getHeader(AccountConstant.CLIENT_COOKIE_KEY);
        Object object = redisTemplate.opsForValue().get(RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + token);
        LoginVO loginVO = ConvertUtils.convertObject(object, LoginVO.class);
        if (loginVO == null) {
            return Result.success(null);
        }

        //如果距过期时间小于一天 续期并将新token存入Cookie返回
        if (loginVO.getExpireAt() - System.currentTimeMillis() < RedisConstant.ONE_DAY_MILLISECOND) {
            String newToken = UUID.randomUUID(true).toString();
            redisTemplate.opsForValue().set(
                    RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + newToken,
                    loginVO,
                    RedisConstant.LOGIN_TOKEN_EXPIRE,
                    TimeUnit.MILLISECONDS
            );
            redisTemplate.delete(RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + token);
            Cookie cookie = new Cookie(AccountConstant.CLIENT_COOKIE_KEY, token);
            cookie.setMaxAge(AccountConstant.COOKIE_LOGIN_TOKEN_EXPIRE / 1000);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return Result.success(loginVO);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                redisTemplate.delete(RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + cookie.getValue());
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
    }

    @Override
    public Result<EmailLoginVerifyVO> emailVerifyLogin(HttpServletRequest request, HttpServletResponse response,
                                                       EmailLoginVerifyDTO emailLoginVerifyDTO) {
        String email = emailLoginVerifyDTO.getEmail();
        String captchaKey = emailLoginVerifyDTO.getCaptchaKey();
        String captcha = (String) redisTemplate.opsForValue()
                .get(captchaKey);
        redisTemplate.delete(captchaKey);
        if (captcha == null || !captcha.equals(emailLoginVerifyDTO.getCaptcha())) {
            throw new LoginErrorException(MessageConstant.CAPTCHA_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        // 用户不存在就注册
        if (user == null) {
            user = User.builder()
                    .email(emailLoginVerifyDTO.getEmail())
                    .nickName(AccountConstant.NICKNAME_DEFAULT_PREFIX + RandomUtil.randomNumbers(11))
                    .sex(AccountGenderEnum.UNKNOWN.getCode())
                    .registerTime(new Date())
                    .status(AccountStatusEnum.NORMAL.getCode())
                    .totalCoinCount(sysSettingProperties.getRegisterCoinCount())
                    .currentCoinCount(sysSettingProperties.getRegisterCoinCount())
                    .theme(AccountConstant.DEFAULT_THEME)
                    .lastLoginTime(new Date())
                    .lastLoginIp(IpUtils.getIpAddress(request))
                    .build();
            save(user);
        }
        //如果存在就更新用户登录信息
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(IpUtils.getIpAddress(request));
        user.setEmail(email);
        update(user, queryWrapper);
        //生成token 存入Redis、Cookie
        String token = UUID.randomUUID(true).toString();
        // 准备响应实体类
        EmailLoginVerifyVO emailLoginVerifyVO = EmailLoginVerifyVO.builder()
                .userId(String.valueOf(user.getId()))
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .token(token)
                .expireAt(System.currentTimeMillis() + RedisConstant.LOGIN_TOKEN_EXPIRE)
                .build();
        redisTemplate.opsForValue().set(
                RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + token,
                emailLoginVerifyVO,
                RedisConstant.LOGIN_TOKEN_EXPIRE,
                TimeUnit.MILLISECONDS
        );

        //删除上一次的token(非必要)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AccountConstant.CLIENT_COOKIE_KEY) && StringUtils.hasText(cookie.getValue())) {
                    redisTemplate.delete(RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.LOGIN_REDIS_KEY + cookie.getValue());
                    break;
                }
            }
        }

        //将新token存入Cookie返回
        Cookie cookie = new Cookie(AccountConstant.CLIENT_COOKIE_KEY, token);
        cookie.setMaxAge(AccountConstant.COOKIE_LOGIN_TOKEN_EXPIRE / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);

        return Result.success(emailLoginVerifyVO);
    }
}