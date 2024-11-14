package com.bilibili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @TableId(type = IdType.AUTO)
    private int id;              //主键
    private String categoryCode; //分类编码
    private String categoryName; //分类名称
    private int pCategoryId;     //父级分类ID
    private String icon;         //分类图标
    private String background;   //分类页面背景图
    private int sort;            //排序
}
