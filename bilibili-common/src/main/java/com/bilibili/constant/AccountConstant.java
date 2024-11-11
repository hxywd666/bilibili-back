package com.bilibili.constant;

/*和账户业务有关的常量*/
public class AccountConstant {
    public static final int CAPTCHA_WIDTH = 100; //验证码图片宽度
    public static final int CAPTCHA_HEIGHT = 40; //验证码图片高度
    public static final int ONE_DAY_MILLISECOND = 60 * 60 * 24 * 1000; //一天的毫秒数
    public static final String CLIENT_KEY_PREFIX = "client:"; //客户端端redis key的前缀
    public static final String ADMIN_KEY_PREFIX = "admin:"; //管理端redis key的前缀
    public static final String CAPTCHA_REDIS_KEY = "captcha:"; //验证码redis key
    public static final int CAPTCHA_EXPIRE = 60000; //验证码有效期(毫秒)
    public static final int DEFAULT_COIN_COUNT = 0; //默认硬币数量
    public static final int DEFAULT_THEME = 1; //默认主题
    public static final int TOKEN_EXPIRE = ONE_DAY_MILLISECOND * 3; //token有效期(3天)
    public static final String LOGIN_REDIS_KEY = "login:"; //登录redis key
    public static final String CLIENT_COOKIE_KEY = "token"; //Web端cookie key
    public static final String ADMIN_COOKIE_KEY = "adminToken"; //管理端的cookie key
}