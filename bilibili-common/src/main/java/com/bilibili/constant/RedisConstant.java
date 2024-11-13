package com.bilibili.constant;

public class RedisConstant {
    /*通用常量*/
    public static final String CLIENT_KEY_PREFIX = "client:"; //客户端redis key的前缀
    public static final String ADMIN_KEY_PREFIX = "admin:"; //管理端redis key的前缀
    public static final String CAPTCHA_REDIS_KEY = "captcha:"; //验证码redis key
    public static final int ONE_DAY_MILLISECOND = 60 * 60 * 24 * 1000; //一天的毫秒数
    public static final int ONE_HOUR_MILLISECOND = 60 * 60 * 1000; //一小时的毫秒数
    public static final int ONE_MIN_MILLISECOND = 60 * 1000; //一分钟的毫秒数

    /*account业务Redis Key*/
    public static final String LOGIN_REDIS_KEY = "login:"; //登录redis key

    /*email业务Redis Key*/
    public static final String EMAIL_REDIS_KEY = "email:"; //邮箱验证码redis key

    /*有效期*/
    public static final int LOGIN_TOKEN_EXPIRE = ONE_DAY_MILLISECOND * 3; //token有效期(3天)
    public static final int PIC_CAPTCHA_EXPIRE = ONE_MIN_MILLISECOND; //图形验证码有效期(1min)
    public static final int EMAIL_CAPTCHA_EXPIRE = ONE_MIN_MILLISECOND * 5; //邮箱验证码有效期(5min)
}
