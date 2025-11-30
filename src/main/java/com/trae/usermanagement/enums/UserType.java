package com.trae.usermanagement.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户类型枚举
 */
public enum UserType {
    ADMIN("管理员"),
    TEACHER("教师"),
    STUDENT("学生"),
    STAFF("职工");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
