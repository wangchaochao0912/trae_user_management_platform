package com.trae.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 班级更新请求DTO类
 */
@Data
public class ClassUpdateRequest {
    @Size(max = 100, message = "班级名称长度不能超过100个字符")
    private String className;

    @Size(max = 50, message = "班级代码长度不能超过50个字符")
    private String classCode;

    @Size(max = 20, message = "年级长度不能超过20个字符")
    private String grade;

    private String description;
    private Long headTeacherId;
    private Boolean isActive;
}
