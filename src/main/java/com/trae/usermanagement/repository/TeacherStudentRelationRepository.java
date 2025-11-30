package com.trae.usermanagement.repository;

import com.trae.usermanagement.entity.TeacherStudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 教师学生关联关系数据访问层
 */
@Repository
public interface TeacherStudentRelationRepository extends JpaRepository<TeacherStudentRelation, Long> {

    /**
     * 根据教师ID查询关联的学生列表
     */
    List<TeacherStudentRelation> findByTeacherId(Long teacherId);

    /**
     * 根据学生ID查询关联的教师列表
     */
    List<TeacherStudentRelation> findByStudentId(Long studentId);

    /**
     * 检查教师和学生的关联关系是否存在
     */
    boolean existsByTeacherIdAndStudentId(Long teacherId, Long studentId);

    /**
     * 根据教师ID和学生ID查询关联关系
     */
    TeacherStudentRelation findByTeacherIdAndStudentId(Long teacherId, Long studentId);

    /**
     * 删除教师的所有关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from TeacherStudentRelation tsr where tsr.teacher.id = :teacherId")
    int deleteByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 删除学生的所有关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from TeacherStudentRelation tsr where tsr.student.id = :studentId")
    int deleteByStudentId(@Param("studentId") Long studentId);

    /**
     * 根据教师ID和学生ID删除关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from TeacherStudentRelation tsr where tsr.teacher.id = :teacherId and tsr.student.id = :studentId")
    int deleteByTeacherIdAndStudentId(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);
}
