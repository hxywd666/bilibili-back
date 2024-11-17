package com.bilibili.utils;

import com.bilibili.exception.ParamErrorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StringTools {

    public static void checkParam(Object param) {
        try {
            Field[] fields = param.getClass().getDeclaredFields();
            boolean notEmpty = false;
            for (Field field : fields) {
                String methodName = "get" + StringTools.upperCaseFirstLetter(field.getName());
                Method method = param.getClass().getMethod(methodName);
                Object object = method.invoke(param);
                if (object != null && object instanceof String && !StringTools.isEmpty(object.toString())) {
                    notEmpty = true;
                    break;
                }
            }
            if (!notEmpty) {
                throw new ParamErrorException("参数校验错误。请检查，确认所有字段都不为空");
            }
        } catch (ParamErrorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamErrorException("参数校验时发生异常");
        }
    }

    public static String upperCaseFirstLetter(String field) {
        if (isEmpty(field)) {
            return field;
        }
        if (field.length() > 1 && Character.isUpperCase(field.charAt(1))) {
            return field;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str) || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static String getRandomString(Integer count) {
        return RandomStringUtils.random(count, true, false);
    }

    public static String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static String encodeByMd5(String originString) {
        return StringTools.isEmpty(originString) ? null : DigestUtils.md5Hex(originString);
    }

    public static boolean pathIsOk(String path) {
        if (StringTools.isEmpty(path)) {
            return true;
        }
        if (path.contains("../") || path.contains("..\\")) {
            return false;
        }
        return true;
    }
}