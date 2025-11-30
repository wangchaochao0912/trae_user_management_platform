package com.trae.entity;

import javax.persistence.*;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类，映射数据库中的users表
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * 用户ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，唯一，不能为空
     */
    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 密码，不能为空
     */
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    /**
     * 用户姓名，不能为空
     */
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    /**
     * 电子邮箱，不能为空
     */
    @Column(nullable = false)
    private String email;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 用户类型，不能为空
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "User type is required")
    @Column(nullable = false)
    private UserType userType;

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
     * 教师关联的学生列表
     */
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherStudentRelation> teacherStudents;

    /**
     * 学生关联的教师列表
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherStudentRelation> studentTeachers;

    /**
     * 学生的班级列表
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClassRelation> classes = new ArrayList<>();

    /**
     * 用户类型枚举
     */
    public enum UserType {
        ADMIN, TEACHER, STUDENT, STAFF
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
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

    public List<TeacherStudentRelation> getTeacherStudents() {
        return teacherStudents;
    }

    public void setTeacherStudents(List<TeacherStudentRelation> teacherStudents) {
        this.teacherStudents = teacherStudents;
    }

    public List<TeacherStudentRelation> getStudentTeachers() {
        return studentTeachers;
    }

    public void setStudentTeachers(List<TeacherStudentRelation> studentTeachers) {
        this.studentTeachers = studentTeachers;
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