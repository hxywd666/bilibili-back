package com.bilibili.handler;

import com.bilibili.constant.MessageConstant;
import com.bilibili.enumeration.HttpStatusEnum;
import com.bilibili.exception.BaseException;
import com.bilibili.exception.LoginErrorException;
import com.bilibili.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/*全局异常处理器*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //专门处理自定义异常
    @ExceptionHandler
    public Result baseExceptionHandler(BaseException ex) {
        log.error("异常信息:{}", ex.getMessage());
        //如果是登录过期异常 状态码设置为401
        if (ex instanceof LoginErrorException && ex.getMessage().equals(MessageConstant.LOGIN_TIMED_OUT)) {
            return Result.error(HttpStatusEnum.UNAUTHORIZED, ex.getMessage());
        }
        //其他异常默认状态码默认都是500
        return Result.error(HttpStatusEnum.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    //专门处理参数校验异常
    @ExceptionHandler
    public Result validationExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("参数校验异常:{}", ex.getMessage());
        return Result.error(HttpStatusEnum.INTERNAL_SERVER_ERROR, MessageConstant.PARAM_ERROR);
    }

    //专门处理IO异常
    @ExceptionHandler
    public Result IOExceptionHandler(IOException ex) {
        log.error("文件异常:{}", ex.getMessage());
        return Result.error(HttpStatusEnum.INTERNAL_SERVER_ERROR, MessageConstant.FILE_UPLOAD_ERROR);
    }
}
