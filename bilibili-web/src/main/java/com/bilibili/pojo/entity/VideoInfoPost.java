package com.bilibili.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VideoInfoPost {
    private String id;
    private String videoCover;
    private String videoName;
    private Long userId;
    private Date createTime;
    private Date lastUpdateTime;
    private Integer pCategoryId;
    private Integer categoryId;
    private Integer status;
    private Integer postType;
    private String originInfo;
    private String tags;
    private String introduction;
    private String interaction;
    private Integer duration;
}

