package com.bilibili.controller;

import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.CategoryException;
import com.bilibili.pojo.dto.CategoryChangeSortDTO;
import com.bilibili.pojo.dto.CategoryDTO;
import com.bilibili.pojo.dto.CategoryIdDTO;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.bilibili.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public Result delCategory(CategoryIdDTO categoryIddto) throws JsonProcessingException {
        int deleted = categoryService.delCategory(categoryIddto.getCategoryId());
        if (deleted <= 0){
            throw new CategoryException("删除失败，为找到删除对象");
        }
        return Result.success();
    }

    @PostMapping("/changeSort")
    public Result changeSort(CategoryChangeSortDTO categoryChangeSortDTO) throws JsonProcessingException {
        int updateSort = categoryService.updateSort(categoryChangeSortDTO.getPCategoryId(), categoryChangeSortDTO.getCategoryIds());
        if (updateSort <= 0){
            return Result.error(HttpStatusEnum.ERROR, "更新失败");
        }
        return Result.success();
    }

}
