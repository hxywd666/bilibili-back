package com.bilibili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

// 用户个人信息实体类
@Data
@Builder
public class User {
    //用户ID
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //用户昵称
    private String nickName;
    //邮箱地址
    private String email;
    //用户密码
    private String password;
    //性别，0表示女，1表示男，2表示未知
    private Integer sex;
    //用户生日
    private Date birthday;
    //用户所在学校
    private String school;
    //用户个人简介
    private String introduction;
    //用户注册时间
    private Date registerTime;
    //用户最后登录时间
    private Date lastLoginTime;
    //用户最后登录的IP地址
    private String lastLoginIp;
    //账号状态，0表示冻结，1表示正常
    private Integer status;
    //个人空间的公告内容
    private String notice;
    //历史硬币的总数量
    private Integer totalCoinCount;
    //当前硬币总量
    private Integer currentCoinCount;
    //用户个人主页主题图片
    private Integer theme;
    //用户个人头像URL
    private String avatar;
}

