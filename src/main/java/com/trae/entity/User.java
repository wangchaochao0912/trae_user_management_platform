package com.trae.entity;

import com.trae.enums.UserType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE user SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = 0")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 用户类型
     */
    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * 删除标志位（0：未删除，1：已删除）
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    /**
     * 教师关联的学生列表
     */
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentTeacherRelation> studentTeacherRelations;

    /**
     * 学生关联的班级列表
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClassRelation> studentClassRelations;

    /**
     * 班级关联的学生列表
     */
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClassRelation> classStudentRelations;

    /**
     * 学生关联的教师列表
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentTeacherRelation> studentTeacherRelationsForStudent;

    /**
     * 初始化方法
     */
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
        this.deleted = false;
    }

    /**
     * 更新方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }
}