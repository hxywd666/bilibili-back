package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryDTO {

    // 带有Fuzzy的是模糊查询
    private Integer categoryId;
    private String categoryCode;
    private String categoryCodeFuzzy;
    private String categoryName;
    private String categoryNameFuzzy;
    private Integer pCategoryId;
    private Integer sort;
    private String iconFuzzy;
    private String background;
    private String backgroundFuzzy;
    private String icon;
    // 前端不传过来这个
    private Integer idOrPcatagoryId;
    // 前端不传过来这个
    private Boolean convertToTree;

    public boolean isDefault() {
        return Objects.isNull(categoryId) &&
                isStringEmpty(categoryCode) &&
                isStringEmpty(categoryCodeFuzzy) &&
                isStringEmpty(categoryName) &&
                isStringEmpty(categoryNameFuzzy) &&
                Objects.isNull(pCategoryId) &&
                Objects.isNull(sort) &&
                isStringEmpty(iconFuzzy) &&
                isStringEmpty(background) &&
                isStringEmpty(backgroundFuzzy) &&
                isStringEmpty(icon);
    }
    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
