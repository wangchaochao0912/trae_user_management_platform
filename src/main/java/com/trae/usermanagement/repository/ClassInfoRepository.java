package com.trae.usermanagement.repository;

import com.trae.usermanagement.entity.ClassInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 班级数据访问层
 */
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long> {

    /**
     * 根据班级代码查询班级
     */
    Optional<ClassInfo> findByClassCode(String classCode);

    /**
     * 根据班级名称查询班级列表
     */
    Page<ClassInfo> findByClassNameContaining(String className, Pageable pageable);

    /**
     * 根据年级查询班级列表
     */
    Page<ClassInfo> findByGrade(String grade, Pageable pageable);

    /**
     * 检查班级代码是否已存在（排除指定ID的班级）
     */
    boolean existsByClassCodeAndIdNot(String classCode, Long id);

    /**
     * 逻辑删除班级
     */
    @Transactional
    @Modifying
    @Query("update ClassInfo c set c.isDeleted = true where c.id = :id")
    int softDeleteById(@Param("id") Long id);

    /**
     * 查询班级是否存在且未被删除
     */
    boolean existsByIdAndIsDeletedFalse(Long id);

    /**
     * 更新班级学生数量
     */
    @Transactional
    @Modifying
    @Query("update ClassInfo c set c.studentCount = c.studentCount + :delta where c.id = :id")
    int updateStudentCount(@Param("id") Long id, @Param("delta") int delta);

    /**
     * 根据条件分页查询班级
     */
    @Query("select c from ClassInfo c where 
            (:className is null or c.className like %:className%) and 
            (:classCode is null or c.classCode like %:classCode%) and 
            (:grade is null or c.grade = :grade) and 
            c.isDeleted = false")
    Page<ClassInfo> findByConditions(
            @Param("className") String className,
            @Param("classCode") String classCode,
            @Param("grade") String grade,
            Pageable pageable);
}
