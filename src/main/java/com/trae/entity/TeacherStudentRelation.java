package com.trae.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 教师学生关系实体类，映射数据库中的teacher_student_relation表
 * 用于记录教师和学生之间的关联关系
 */
@Entity
@Table(name = "teacher_student_relation")
public class TeacherStudentRelation {

    /**
     * 关系ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的教师用户
     */
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /**
     * 关联的学生用户
     */
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * 关系状态，默认为true（活跃）
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * 创建时间，不能为空，不可更新
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle callbacks
    /**
     * 持久化前回调，设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * 更新前回调，设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}