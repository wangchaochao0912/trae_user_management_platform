package com.trae.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 教师学生关联关系请求DTO类
 */
@Data
public class TeacherStudentRelationRequest {
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    private String relationType;
    private String notes;
}
