package com.bilibili.controller;

import com.bilibili.pojo.dto.EmailLoginVerifyDTO;
import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.pojo.vo.EmailLoginVerifyVO;
import com.bilibili.pojo.vo.LoginVO;
import com.bilibili.result.Result;
import com.bilibili.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/*账户相关接口*/
@RestController
@RequestMapping("/account")
@Validated
public class AccountController {
    @Autowired
    private AccountService accountService;

    //验证码接口
    @GetMapping("/checkCode")
    public Result<CheckCodeVO> getCheckCode() {
        return accountService.getCheckCode();
    }

    //密码登录接口
    @PostMapping("/password/login")
    public Result<LoginVO> login(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody LoginDTO loginDTO) {
        return accountService.login(request, response,loginDTO);
    }

    //邮箱验证登录接口
    @PostMapping("/email/login")
    public Result<EmailLoginVerifyVO> emailVerifyLogin(HttpServletRequest request, HttpServletResponse response,
                                                       @Valid @RequestBody EmailLoginVerifyDTO emailLoginVerifyDTO) {
        return accountService.emailVerifyLogin(request, response, emailLoginVerifyDTO);
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
