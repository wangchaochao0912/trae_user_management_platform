package com.trae.repository;

import com.trae.entity.StudentClassRelation;
import com.trae.entity.User;
import com.trae.entity.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生班级关系数据访问接口，继承JpaRepository，提供学生班级关系相关的数据库操作
 */
@Repository
public interface StudentClassRelationRepository extends JpaRepository<StudentClassRelation, Long> {

    /**
     * 根据学生查找活跃的班级关系
     * @param student 学生对象
     * @return 学生的活跃班级关系列表
     */
    List<StudentClassRelation> findActiveByStudent(User student);

    /**
     * 根据班级查找活跃的学生关系
     * @param classInfo 班级对象
     * @return 班级的活跃学生关系列表
     */
    List<StudentClassRelation> findActiveByClassInfo(ClassInfo classInfo);

    /**
     * 根据学生和班级查找活跃的关系
     * @param student 学生对象
     * @param classInfo 班级对象
     * @return 学生和班级之间的活跃关系
     */
    Optional<StudentClassRelation> findActiveByStudentAndClassInfo(User student, ClassInfo classInfo);
}
