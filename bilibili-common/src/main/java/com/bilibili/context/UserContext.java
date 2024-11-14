package com.bilibili.context;

public class UserContext {

    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();

    // 将 userId 存入 ThreadLocal
    public static void setUserId(Long userId) {
        userIdThreadLocal.set(userId);
    }

    // 获取当前线程的 userId
    public static Long getUserId() {
        return userIdThreadLocal.get();
    }

    // 清除当前线程的 userId
    public static void clearUserId() {
        userIdThreadLocal.remove();
    }
}
