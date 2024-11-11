package com.bilibili.enumeration;

public enum AccountGenderEnum {
    FEMALE(0, "女"),
    MALE(1, "男"),
    UNKNOWN(2, "未知");

    private final int code;
    private final String description;

    AccountGenderEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
