package com.trae.usermanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级响应DTO类
 */
@Data
public class ClassResponse {
    private Long id;
    private String classCode;
    private String className;
    private String grade;
    private String description;
    private Long headTeacherId;
    private String headTeacherName;
    private Integer studentCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}