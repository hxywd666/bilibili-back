package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Integer categoryId;              //主键
    private String categoryCode; //分类编码
    private String categoryName; //分类名称
    private Integer pCategoryId;     //父级分类ID
    private String icon;         //分类图标
    private String background;   //分类页面背景图
}
