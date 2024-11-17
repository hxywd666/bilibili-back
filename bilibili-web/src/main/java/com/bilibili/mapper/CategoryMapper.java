package com.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> selectCategoryByParam(@Param("categoryQuery") CategoryQueryDTO categoryQuery);
}
