package com.bilibili.controller;

import com.bilibili.constant.RedisConstant;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.bilibili.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/loadAllCategory")
    public Result<List<CategoryVO>> loadCategory(CategoryQueryDTO categoryQuery) {
        return categoryService.loadCategory(categoryQuery);
    }
}
