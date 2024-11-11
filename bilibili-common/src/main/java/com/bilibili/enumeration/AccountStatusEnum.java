package com.bilibili.enumeration;

public enum AccountStatusEnum {
    FROZEN(0, "冻结"),
    NORMAL(1, "正常");

    private final int code;
    private final String description;

    AccountStatusEnum(int code, String description) {
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
