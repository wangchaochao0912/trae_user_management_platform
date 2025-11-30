package com.trae.enums;

/**
 * 用户类型枚举类
 */
public enum UserType {
    ADMIN("管理员"),
    TEACHER("教师"),
    STUDENT("学生"),
    STAFF("职工");

    private String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}