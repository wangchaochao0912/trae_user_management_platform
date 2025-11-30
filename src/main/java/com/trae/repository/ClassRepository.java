package com.trae.repository;

import com.trae.entity.Class;
import com.trae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 班级Repository接口
 */
@Repository
public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {

    /**
     * 根据班级名称查询班级
     * @param name 班级名称
     * @return 班级信息
     */
    Optional<Class> findByName(String name);

    /**
     * 根据班级名称模糊查询班级列表
     * @param name 班级名称
     * @return 班级列表
     */
    List<Class> findByNameContaining(String name);

    /**
     * 根据班级ID查询关联的学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    @Query("SELECT u FROM User u JOIN StudentClassRelation scr ON u.id = scr.studentId WHERE scr.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);

    /**
     * 根据学生ID查询关联的班级列表
     * @param studentId 学生ID
     * @return 班级列表
     */
    @Query("SELECT c FROM Class c JOIN StudentClassRelation scr ON c.id = scr.classId WHERE scr.studentId = :studentId")
    List<Class> findClassesByStudentId(@Param("studentId") Long studentId);
}
