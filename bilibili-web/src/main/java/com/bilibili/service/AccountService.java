package com.bilibili.service;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.dto.RegisterDTO;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.LoginVO;
import com.bilibili.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountService {
    Result<CheckCodeVO> getCheckCode();
    Result<Boolean> register(RegisterDTO registerDTO);
    Result<LoginVO> login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO);
    Result<LoginVO> autoLogin(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    Result<EmailLoginVerifyVO> emailVerifyLogin(HttpServletRequest request, HttpServletResponse response, EmailLoginVerifyDTO emailLoginVerifyDTO);
}