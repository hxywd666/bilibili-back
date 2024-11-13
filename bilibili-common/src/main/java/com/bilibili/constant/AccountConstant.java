package com.bilibili.constant;

/*和账户业务有关的常量*/
public class AccountConstant {
    public static final int CAPTCHA_WIDTH = 120; //验证码图片宽度
    public static final int CAPTCHA_HEIGHT = 32; //验证码图片高度
    public static final int DEFAULT_COIN_COUNT = 0; //默认硬币数量
    public static final int DEFAULT_THEME = 1; //默认主题
    public static final String CLIENT_COOKIE_KEY = "token"; //Web端cookie key
    public static final String ADMIN_COOKIE_KEY = "adminToken"; //管理端的cookie key
    public static final int COOKIE_LOGIN_TOKEN_EXPIRE = 60 * 60 * 24 * 1000 * 3;
    public static final String NICKNAME_DEFAULT_PREFIX = "bili_"; //默认用户昵称的前缀
}