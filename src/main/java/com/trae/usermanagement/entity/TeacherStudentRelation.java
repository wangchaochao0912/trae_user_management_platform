package com.trae.usermanagement.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 教师学生关联关系实体类
 */
@Data
@Entity
@Table(name = "teacher_student_relation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teacher_id", "student_id"})
})
public class TeacherStudentRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "教师不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @NotNull(message = "学生不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    private String relationType; // 例如：班主任、任课老师等
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
