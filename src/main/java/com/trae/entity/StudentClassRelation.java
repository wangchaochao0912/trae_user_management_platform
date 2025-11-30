package com.trae.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 学生班级关联实体类
 */
@Data
@Entity
@Table(name = "student_class_relation")
@DynamicInsert
@DynamicUpdate
public class StudentClassRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生ID
     */
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * 班级ID
     */
    @Column(name = "class_id", nullable = false)
    private Long classId;

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
     * 学生实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User student;

    /**
     * 班级实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Class classEntity;

    /**
     * 初始化方法
     */
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
    }

    /**
     * 更新方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }
}