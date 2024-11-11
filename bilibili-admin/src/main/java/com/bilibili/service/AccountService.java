package com.bilibili.service;

import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountService {
    Result<CheckCodeVO> getCheckCode();

    Result login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
