package com.bilibili.service;

import com.bilibili.pojo.dto.CategoryDTO;
import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.vo.CategoryVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CategoryService {
    List<CategoryVO> loadCategory(CategoryQueryDTO categoryQuery) throws JsonProcessingException;
}
