package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.constant.CategoryConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.exception.BaseException;
import com.bilibili.exception.CategoryException;
import com.bilibili.mapper.CategoryMapper;
import com.bilibili.pojo.dto.CategoryDTO;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 保存新增的分类
    @Override
    public void SaveCategory(CategoryDTO categoryDTO,HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException {
        // CategoryCode 是要求每一个分类都要唯一的
        Category getCategory = categoryMapper.selectByCategoryCode(categoryDTO.getCategoryCode());
        //  1. 如果传过来的没有分类id，则表示为新增，根据code查询如果存在表示已经存在
        //  2. 如果传过来了分类id，并且根据名称也能查到，那么必须传过来的id和查到的id相等表示同一个，不然就是已存在
        if (categoryDTO.getCategoryId() == null && getCategory != null){
            throw new CategoryException("分类已存在");
        }
        if (categoryDTO.getCategoryId() != null && getCategory != null && categoryDTO.getCategoryId().equals(getCategory.getId())) {
            int update = updateCategory(categoryDTO, getCategory);
            if (update <= 0){
                throw new CategoryException("更新失败");
            }
            saveToRedis(new CategoryQueryDTO());
            return;
        }
        // 转为存储对象
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        // 查询最大的当前排序序号，并加 1
        Integer maxSort = categoryMapper.selectMaxSort(category.getPCategoryId());
        category.setSort(maxSort + 1);
        // 存入数据库
        categoryMapper.insert(category);
        saveToRedis(new CategoryQueryDTO());
    }
    public int updateCategory(CategoryDTO categoryDTO,Category category){
        BeanUtils.copyProperties(categoryDTO,category);
        int update = categoryMapper.updateById(category);
        return update;
    }


    // 根据查询条件，模糊查询并且返回树状结构给前端
    @Override
    public List<CategoryVO> loadCategory(CategoryQueryDTO categoryQuery) throws JsonProcessingException {
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
        return categoryVOS;
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
    // 这里删除以一级分类以及他对应的二级分类数据
    @Override
    public int delCategory(Integer categoryId) throws JsonProcessingException {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getId, categoryId)
                .or()
                .eq(Category::getPCategoryId, categoryId);
        int delete = categoryMapper.delete(lambdaQueryWrapper);
        if(delete <= 0){
            throw new CategoryException("删除失败");
        }
        saveToRedis(new CategoryQueryDTO());
        return delete;
    }

    // 更新排序信息，这里主要针对的是根据pid分类标签进行更新排序信息
    @Override
    public int updateSort(Integer pCategoryId, String ids) throws JsonProcessingException {
        ArrayList<Category> updateCategory = new ArrayList<>();
        String[] idArray = ids.split(",");
        Integer sort = 0;
        for (String strId : idArray) {
            Category category = new Category();
            category.setId(Integer.parseInt(strId));
            category.setPCategoryId(pCategoryId);
            category.setSort(++sort);
            updateCategory.add(category);
        }
        int update = categoryMapper.updateSortBatch(updateCategory);
        saveToRedis(new CategoryQueryDTO());
        return update;
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

    private void saveToRedis(CategoryQueryDTO categoryQuery) throws JsonProcessingException {
        // 删除所有以 CATEGOTY_LIST_KEY 开头的键
        //String pattern = RedisConstant.CATEGOTY_LIST_KEY + "*"; // 这里用 * 通配符匹配所有
        //Set<String> keys = redisTemplate.keys(pattern); // 获取所有匹配的键
        //
        //if (keys != null && !keys.isEmpty()) {
        //    // 删除键
        //    redisTemplate.delete(keys);
        //}
        categoryQuery.setConvertToTree(true);
        List<CategoryVO> categories = loadCategory(categoryQuery);
        redisTemplate.opsForValue().set(RedisConstant.CATEGOTY_LIST_KEY ,categories);

    }
}
