package com.bilibili.handler;

import com.bilibili.constant.MessageConstant;
import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.BaseException;
import com.bilibili.exception.LoginErrorException;
import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*全局异常处理器*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息:{}", ex.getMessage());
        //如果是登录过期异常 状态码设置为401
        if (ex instanceof LoginErrorException && ex.getMessage().equals(MessageConstant.LOGIN_TIMED_OUT)) {
            return Result.error(HttpStatusEnum.UNAUTHORIZED, ex.getMessage());
        }
        //其他异常默认状态码默认都是500
        return Result.error(HttpStatusEnum.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
