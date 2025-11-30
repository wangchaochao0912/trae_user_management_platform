package com.trae.entity;

import javax.persistence.*;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 班级信息实体类，映射数据库中的class_info表
 */
@Entity
@Table(name = "class_info")
public class ClassInfo {

    /**
     * 班级ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 班级名称，不能为空
     */
    @NotBlank(message = "Class name is required")
    @Column(nullable = false)
    private String name;

    /**
     * 班级描述
     */
    private String description;

    /**
     * 删除标记，默认为false
     */
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * 创建时间，不能为空，不可更新
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // Relationships
    /**
     * 班级中的学生列表
     */
    @OneToMany(mappedBy = "classInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClassRelation> students = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public List<StudentClassRelation> getStudents() {
        return students;
    }

    public void setStudents(List<StudentClassRelation> students) {
        this.students = students;
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