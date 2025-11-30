package com.trae.repository;

import com.trae.entity.TeacherStudentRelation;
import com.trae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 教师学生关系数据访问接口，继承JpaRepository，提供教师学生关系相关的数据库操作
 */
@Repository
public interface TeacherStudentRelationRepository extends JpaRepository<TeacherStudentRelation, Long> {

    /**
     * 根据教师查询活跃的师生关系
     * @param teacher 教师用户对象
     * @return 活跃的师生关系列表
     */
    @Query("SELECT r FROM TeacherStudentRelation r WHERE r.teacher = :teacher AND r.active = true")
    List<TeacherStudentRelation> findActiveByTeacher(@Param("teacher") User teacher);

    /**
     * 根据学生查询活跃的师生关系
     * @param student 学生用户对象
     * @return 活跃的师生关系列表
     */
    @Query("SELECT r FROM TeacherStudentRelation r WHERE r.student = :student AND r.active = true")
    List<TeacherStudentRelation> findActiveByStudent(@Param("student") User student);

    /**
     * 根据教师和学生查询活跃的师生关系
     * @param teacher 教师用户对象
     * @param student 学生用户对象
     * @return 活跃的师生关系对象
     */
    @Query("SELECT r FROM TeacherStudentRelation r WHERE r.teacher = :teacher AND r.student = :student AND r.active = true")
    TeacherStudentRelation findActiveByTeacherAndStudent(@Param("teacher") User teacher, @Param("student") User student);
}
