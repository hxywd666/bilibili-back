package com.bilibili.controller;

import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.CategoryException;
import com.bilibili.pojo.dto.CategoryDTO;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.bilibili.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @PostMapping("/saveCategory")
    public Result saveCategory(HttpServletRequest request, HttpServletResponse response,CategoryDTO categoryDTO ) throws JsonProcessingException {
        categoryService.SaveCategory(categoryDTO,request,response);
        return Result.success();
    }
    @PostMapping("/loadCategory")
    public Result loadCategory(HttpServletRequest request, HttpServletResponse response, CategoryQueryDTO categoryQuery) throws JsonProcessingException {
        List<CategoryVO> categoryList = categoryService.loadCategory(categoryQuery);
        return Result.success(categoryList);
    }

    @PostMapping("/delCategory")
    public Result delCategory(@NotNull Integer categoryId) throws JsonProcessingException {
        int deleted = categoryService.delCategory(categoryId);
        if (deleted <= 0){
            throw new CategoryException("删除失败，为找到删除对象");
        }
        return Result.success();
    }

    @PostMapping("/changeSort")
    public Result changeSort(@NotNull Integer pCategoryId, @NotEmpty String categoryIds) throws JsonProcessingException {
        int updateSort = categoryService.updateSort(pCategoryId, categoryIds);
        if (updateSort <= 0){
            return Result.error(HttpStatusEnum.ERROR, "更新失败");
        }
        return Result.success();
    }

}
