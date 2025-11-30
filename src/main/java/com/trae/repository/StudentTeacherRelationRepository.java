package com.trae.repository;

import com.trae.entity.StudentTeacherRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生教师关联Repository接口
 */
@Repository
public interface StudentTeacherRelationRepository extends JpaRepository<StudentTeacherRelation, Long>, JpaSpecificationExecutor<StudentTeacherRelation> {

    /**
     * 根据教师ID查询关联列表
     * @param teacherId 教师ID
     * @return 关联列表
     */
    List<StudentTeacherRelation> findByTeacherId(Long teacherId);

    /**
     * 根据学生ID查询关联列表
     * @param studentId 学生ID
     * @return 关联列表
     */
    List<StudentTeacherRelation> findByStudentId(Long studentId);

    /**
     * 根据学生ID和教师ID查询关联
     * @param studentId 学生ID
     * @param teacherId 教师ID
     * @return 关联信息
     */
    StudentTeacherRelation findByStudentIdAndTeacherId(Long studentId, Long teacherId);

    /**
     * 删除学生和教师之间的关联
     * @param studentId 学生ID
     * @param teacherId 教师ID
     */
    @Query("DELETE FROM StudentTeacherRelation str WHERE str.studentId = :studentId AND str.teacherId = :teacherId")
    void deleteByStudentIdAndTeacherId(@Param("studentId") Long studentId, @Param("teacherId") Long teacherId);
}
