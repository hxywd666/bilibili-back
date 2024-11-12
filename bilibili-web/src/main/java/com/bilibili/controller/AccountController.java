package com.bilibili.controller;

import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.dto.RegisterDTO;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.pojo.vo.LoginVO;
import com.bilibili.result.Result;
import com.bilibili.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/*账户相关接口*/
@RestController
@RequestMapping("/account")
@Validated
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    //验证码接口
    @PostMapping("/checkCode")
    public Result<CheckCodeVO> getCheckCode() {
        return accountService.getCheckCode();
    }

    //注册接口
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterDTO registerDTO) {
        return accountService.register(registerDTO);
    }

    //登录接口
    @PostMapping("/login")
    public Result<LoginVO> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO loginDTO) {
        return accountService.login(request, response,loginDTO);
    }

    //自动登录接口
    @PostMapping("/autoLogin")
    public Result<LoginVO> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return accountService.autoLogin(request, response);
    }

    //退出登录接口
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        accountService.logout(request, response);
        return Result.success();
    }
}
