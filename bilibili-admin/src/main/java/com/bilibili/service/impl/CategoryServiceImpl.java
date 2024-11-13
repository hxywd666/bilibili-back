package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.pojo.entity.Category;
import com.bilibili.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
