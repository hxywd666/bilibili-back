package com.bilibili.controller;

import com.bilibili.constant.RedisConstant;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.bilibili.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("loadAllCategory")
    public Result loadCategory(HttpServletRequest request, HttpServletResponse response, CategoryQueryDTO categoryQuery) throws JsonProcessingException {


        List<CategoryVO> categoryList = categoryService.loadCategory(categoryQuery);
        return Result.success(categoryList);
    }

}
