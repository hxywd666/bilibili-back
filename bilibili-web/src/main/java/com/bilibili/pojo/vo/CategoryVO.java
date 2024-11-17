package com.bilibili.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {
    private Integer categoryId; // 实体类中的属性名称
    private String categoryCode; //分类编码
    private String categoryName; //分类名称
    @JsonProperty("pCategoryId")
    private Integer pCategoryId;     //父级分类ID
    private String icon;         //分类图标
    private String background;   //分类页面背景图
    private int sort;            //排序
    private List<CategoryVO> children; // 子类目
}

