package com.bilibili.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryChangeSortDTO {

    @NotNull
    private Integer pCategoryId;

    @NotEmpty
    private String categoryIds;
}
