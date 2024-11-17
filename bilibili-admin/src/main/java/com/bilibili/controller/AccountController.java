package com.bilibili.controller;

import com.bilibili.pojo.dto.LoginDTO;
import com.bilibili.pojo.vo.CheckCodeVO;
import com.bilibili.result.Result;
import com.bilibili.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/checkCode")
    public Result<CheckCodeVO> getCheckCode() {
        return accountService.getCheckCode();
    }

    @PostMapping("/login")
    public Result<String> login (HttpServletRequest request, HttpServletResponse response, @Valid LoginDTO loginDTO) {
        return accountService.login(request, response,loginDTO);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        accountService.logout(request, response);
        return Result.success();
    }
}
