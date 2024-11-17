package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.CategoryConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.bilibili.service.CategoryService;
import com.bilibili.utils.ConvertUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY = RedisConstant.CLIENT_KEY_PREFIX + RedisConstant.CATEGOTY_LIST_KEY;

    @Override
    public Result<List<CategoryVO>> loadCategory(CategoryQueryDTO categoryQuery) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY))) {
            Object object = redisTemplate.opsForValue().get(REDIS_KEY);
            ArrayList<CategoryVO> categoryVOS = ConvertUtils.convertObject(object, ArrayList.class);
            return Result.success(categoryVOS);
        }
        categoryQuery.setConvertToTree(true);
        List<Category> categories = categoryMapper.selectCategoryByParam(categoryQuery);
        // 转为返回给前端类型
        ArrayList<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories) {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVO.setCategoryId(category.getId());
            categoryVOS.add(categoryVO);
        }
        if (categoryQuery.getConvertToTree() != null && categoryQuery.getConvertToTree()){
            convertToTree(categoryVOS, CategoryConstant.PID_ZERO);
        }
        sortCategories(categoryVOS);
        return Result.success(categoryVOS);
    }

    // 每一级按照sort排序
    private void sortCategories(List<CategoryVO> categories) {
        if (categories == null || categories.isEmpty()) {
            return;
        }

        categories.sort(Comparator.comparingInt(CategoryVO::getSort));

        for (CategoryVO categoryVO : categories) {
            sortCategories(categoryVO.getChildren());
        }
    }

    // 转为树形结构
    private List<CategoryVO> convertToTree(List<CategoryVO> categories,Integer pid){
        List<CategoryVO> children = new ArrayList<>();
        for(CategoryVO category: categories){
            if (category.getCategoryId() != null && category.getPCategoryId() != null && category.getPCategoryId().equals(pid)){
                category.setChildren(convertToTree(categories,category.getCategoryId()));
                children.add(category);
            }
        }
        return children;
    }
}
