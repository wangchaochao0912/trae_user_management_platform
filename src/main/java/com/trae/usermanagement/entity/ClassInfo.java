package com.trae.usermanagement.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 班级实体类
 */
@Data
@Entity
@Table(name = "class_info")
@Where(clause = "is_deleted = false")
public class ClassInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "班级名称不能为空")
    private String className;

    @NotBlank(message = "班级代码不能为空")
    @Column(unique = true)
    private String classCode;

    private String grade;
    private String description;
    private Integer studentCount = 0;

    // 逻辑删除标记
    private boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 关联关系
    @OneToMany(mappedBy = "classInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<ClassStudentRelation> studentRelations = new HashSet<>();
}
