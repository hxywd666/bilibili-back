package com.bilibili.service;

import com.bilibili.pojo.dto.CategoryDTO;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.entity.Category;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CategoryService {

    void SaveCategory(CategoryDTO categoryDTO,HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException;

    Result<List<CategoryVO>> loadCategory(CategoryQueryDTO categoryQuery) throws JsonProcessingException;

    void delCategory(Integer id) throws JsonProcessingException;

    void updateSort(Integer pCategoryId, String ids) throws JsonProcessingException;
}
