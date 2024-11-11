package com.bilibili.result;

import com.bilibili.enumeration.HttpStatusEnum;
import lombok.Data;
import java.io.Serializable;

/*通用返回结果*/
@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String info;
    private String status;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = HttpStatusEnum.OK.getCode();
        result.info = "请求成功";
        result.status = "success";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = HttpStatusEnum.OK.getCode();
        result.info = "请求成功";
        result.status = "success";
        return result;
    }

    public static <T> Result<T> error(HttpStatusEnum code, String message) {
        Result<T> result = new Result<>();
        result.code = code.getCode();
        result.info = message;
        result.status = "error";
        return result;
    }
}
