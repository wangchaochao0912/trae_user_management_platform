package com.trae.repository;

import com.trae.entity.User;
import com.trae.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据用户类型查询用户列表
     * @param userType 用户类型
     * @return 用户列表
     */
    List<User> findByUserType(UserType userType);

    /**
     * 根据姓名模糊查询用户列表
     * @param name 姓名
     * @return 用户列表
     */
    List<User> findByNameContaining(String name);

    /**
     * 查询教师列表
     * @return 教师列表
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'TEACHER'")
    List<User> findTeachers();

    /**
     * 查询学生列表
     * @return 学生列表
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'STUDENT'")
    List<User> findStudents();

    /**
     * 根据教师ID查询关联的学生列表
     * @param teacherId 教师ID
     * @return 学生列表
     */
    @Query("SELECT u FROM User u JOIN StudentTeacherRelation str ON u.id = str.studentId WHERE str.teacherId = :teacherId")
    List<User> findStudentsByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 根据学生ID查询关联的教师列表
     * @param studentId 学生ID
     * @return 教师列表
     */
    @Query("SELECT u FROM User u JOIN StudentTeacherRelation str ON u.id = str.teacherId WHERE str.studentId = :studentId")
    List<User> findTeachersByStudentId(@Param("studentId") Long studentId);
}
