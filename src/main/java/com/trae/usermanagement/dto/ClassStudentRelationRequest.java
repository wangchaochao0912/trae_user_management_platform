package com.trae.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 班级学生关联关系请求DTO类
 */
@Data
public class ClassStudentRelationRequest {
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    private String studentNumber;
    private String seatNumber;
    private String notes;
}
