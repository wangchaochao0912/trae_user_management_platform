package com.trae.usermanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级DTO类，用于API响应
 */
@Data
public class ClassDTO {
    private Long id;
    private String className;
    private String classCode;
    private String grade;
    private String description;
    private Integer studentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
