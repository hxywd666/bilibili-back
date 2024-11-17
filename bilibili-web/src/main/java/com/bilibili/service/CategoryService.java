package com.bilibili.service;

import com.bilibili.pojo.dto.CategoryQueryDTO;
import com.bilibili.pojo.vo.CategoryVO;
import com.bilibili.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface CategoryService {
    Result<List<CategoryVO>> loadCategory(CategoryQueryDTO categoryQuery);
}
