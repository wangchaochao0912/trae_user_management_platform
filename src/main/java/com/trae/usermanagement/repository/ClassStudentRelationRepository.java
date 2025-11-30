package com.trae.usermanagement.repository;

import com.trae.usermanagement.entity.ClassStudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级学生关联关系数据访问层
 */
@Repository
public interface ClassStudentRelationRepository extends JpaRepository<ClassStudentRelation, Long> {

    /**
     * 根据班级ID查询关联的学生列表
     */
    List<ClassStudentRelation> findByClassInfoId(Long classId);

    /**
     * 根据学生ID查询关联的班级列表
     */
    List<ClassStudentRelation> findByStudentId(Long studentId);

    /**
     * 检查班级和学生的关联关系是否存在
     */
    boolean existsByClassInfoIdAndStudentId(Long classId, Long studentId);

    /**
     * 根据班级ID和学生ID查询关联关系
     */
    ClassStudentRelation findByClassInfoIdAndStudentId(Long classId, Long studentId);

    /**
     * 删除班级的所有关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from ClassStudentRelation csr where csr.classInfo.id = :classId")
    int deleteByClassInfoId(@Param("classId") Long classId);

    /**
     * 删除学生的所有关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from ClassStudentRelation csr where csr.student.id = :studentId")
    int deleteByStudentId(@Param("studentId") Long studentId);

    /**
     * 根据班级ID和学生ID删除关联关系
     */
    @Transactional
    @Modifying
    @Query("delete from ClassStudentRelation csr where csr.classInfo.id = :classId and csr.student.id = :studentId")
    int deleteByClassInfoIdAndStudentId(@Param("classId") Long classId, @Param("studentId") Long studentId);

    /**
     * 统计班级中的学生数量
     */
    long countByClassInfoId(Long classId);
}
