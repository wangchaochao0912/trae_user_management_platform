package com.trae.repository;

import com.trae.entity.StudentClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生班级关联Repository接口
 */
@Repository
public interface StudentClassRelationRepository extends JpaRepository<StudentClassRelation, Long>, JpaSpecificationExecutor<StudentClassRelation> {

    /**
     * 根据班级ID查询关联列表
     * @param classId 班级ID
     * @return 关联列表
     */
    List<StudentClassRelation> findByClassId(Long classId);

    /**
     * 根据学生ID查询关联列表
     * @param studentId 学生ID
     * @return 关联列表
     */
    List<StudentClassRelation> findByStudentId(Long studentId);

    /**
     * 根据学生ID和班级ID查询关联
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 关联信息
     */
    StudentClassRelation findByStudentIdAndClassId(Long studentId, Long classId);

    /**
     * 删除学生和班级之间的关联
     * @param studentId 学生ID
     * @param classId 班级ID
     */
    @Query("DELETE FROM StudentClassRelation scr WHERE scr.studentId = :studentId AND scr.classId = :classId")
    void deleteByStudentIdAndClassId(@Param("studentId") Long studentId, @Param("classId") Long classId);
}
