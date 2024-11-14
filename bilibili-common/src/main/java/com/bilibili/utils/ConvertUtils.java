package com.bilibili.utils;

import com.alibaba.fastjson.JSON;

public class ConvertUtils {

    //将从Redis中获取的对象转换为指定类型的实体类
    public static <T> T convertObject(Object object, Class<T> targetType) {
        // 将对象序列化为 JSON 字符串
        String jsonString = JSON.toJSONString(object);
        // 将 JSON 字符串反序列化为指定类型的实体类
        return JSON.parseObject(jsonString, targetType);
    }
}
