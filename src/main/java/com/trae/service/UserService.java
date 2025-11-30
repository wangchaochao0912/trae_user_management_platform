package com.trae.service;

import com.trae.entity.User;
import com.trae.entity.TeacherStudentRelation;
import com.trae.entity.StudentClassRelation;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口，定义用户相关的业务逻辑方法
 */
public interface UserService {

    /**
     * 获取所有未删除的用户
     * @return 未删除的用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据ID获取未删除的用户
     * @param id 用户ID
     * @return 未删除的用户Optional对象
     */
    Optional<User> getUserById(Long id);

    /**
     * 根据用户名获取未删除的用户
     * @param username 用户名
     * @return 未删除的用户Optional对象
     */
    Optional<User> getUserByUsername(String username);

    /**
     * 根据用户类型获取未删除的用户
     * @param userType 用户类型
     * @return 未删除的用户列表
     */
    List<User> getUsersByType(User.UserType userType);

    /**
     * 创建新用户
     * @param user 用户对象
     * @return 创建后的用户对象
     * @throws IllegalArgumentException 如果用户名已存在或用户类型为空
     */
    User createUser(User user);

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 更新后的用户对象
     * @return 更新后的用户对象
     * @throws IllegalArgumentException 如果用户不存在
     */
    User updateUser(Long id, User user);

    /**
     * 逻辑删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    // Teacher-Student relations
    /**
     * 根据教师ID获取该教师的所有学生
     * @param teacherId 教师ID
     * @return 教师的学生列表
     * @throws IllegalArgumentException 如果教师ID无效
     */
    List<TeacherStudentRelation> getStudentsByTeacher(Long teacherId);

    /**
     * 根据学生ID获取该学生的所有教师
     * @param studentId 学生ID
     * @return 学生的教师列表
     * @throws IllegalArgumentException 如果学生ID无效
     */
    List<TeacherStudentRelation> getTeachersByStudent(Long studentId);

    /**
     * 分配学生给教师
     * @param teacherId 教师ID
     * @param studentId 学生ID
     * @return 创建的师生关系对象
     * @throws IllegalArgumentException 如果教师ID或学生ID无效，或关系已存在
     */
    TeacherStudentRelation assignStudentToTeacher(Long teacherId, Long studentId);

    /**
     * 解除学生与教师的关系
     * @param teacherId 教师ID
     * @param studentId 学生ID
     * @throws IllegalArgumentException 如果教师ID或学生ID无效
     */
    void unassignStudentFromTeacher(Long teacherId, Long studentId);

    // Student-Class relations
    /**
     * 分配学生到班级
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 新创建的学生班级关系
     */
    StudentClassRelation assignStudentToClass(Long studentId, Long classId);

    /**
     * 解除学生与班级的关联
     * @param studentId 学生ID
     * @param classId 班级ID
     */
    void unassignStudentFromClass(Long studentId, Long classId);

    /**
     * 获取学生的所有班级
     * @param studentId 学生ID
     * @return 学生的班级关系列表
     */
    List<StudentClassRelation> getClassesByStudent(Long studentId);
}
