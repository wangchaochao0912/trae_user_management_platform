package com.trae.entity;

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
 * 班级实体类
 */
@Data
@Entity
@Table(name = "class")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE class SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = 0")
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 班级名称
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * 班级描述
     */
    @Column(name = "description")
    private String description;

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
     * 班级关联的学生列表
     */
    @OneToMany(mappedBy = "class", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentClassRelation> studentClassRelations;

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