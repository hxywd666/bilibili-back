package com.bilibili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id; // 实体类中的属性名称
    private String categoryCode; //分类编码
    private String categoryName; //分类名称
    private Integer pCategoryId;     //父级分类ID
    private String icon;         //分类图标
    private String background;   //分类页面背景图
    private int sort;            //排序
    @TableField(exist = false)   // 数据库中不存在该字段，递归调用填充
    private List<Category> children; // 子类目
}
