package com.bilibili.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfo {
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
    private Integer playCount;
    private Integer likeCount;
    private Integer danmuCount;
    private Integer commentCount;
    private Integer coinCount;
    private Integer collectCount;
    private Integer recommendType;
    private Date lastPlayTime;
}

