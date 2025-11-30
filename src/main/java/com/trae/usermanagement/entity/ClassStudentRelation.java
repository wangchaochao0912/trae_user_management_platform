package com.trae.usermanagement.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 班级学生关联关系实体类
 */
@Data
@Entity
@Table(name = "class_student_relation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"class_id", "student_id"})
})
public class ClassStudentRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "班级不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @NotNull(message = "学生不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    private String studentNumber; // 学号
    private String seatNumber;
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
