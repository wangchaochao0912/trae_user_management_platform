package com.trae.service;

import com.trae.entity.User;
import com.trae.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 用户Service接口
 */
public interface UserService {

    /**
     * 保存用户
     * @param user 用户信息
     * @return 用户信息
     */
    User saveUser(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 用户信息
     */
    User updateUser(User user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    void deleteUserById(Long id);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    Optional<User> findUserById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findUserByUsername(String username);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAllUsers();

    /**
     * 分页查询用户
     * @param pageable 分页信息
     * @return 用户分页列表
     */
    Page<User> findUsersByPage(Pageable pageable);

    /**
     * 根据用户类型查询用户列表
     * @param userType 用户类型
     * @return 用户列表
     */
    List<User> findUsersByUserType(UserType userType);

    /**
     * 根据姓名模糊查询用户列表
     * @param name 姓名
     * @return 用户列表
     */
    List<User> findUsersByNameContaining(String name);

    /**
     * 查询教师列表
     * @return 教师列表
     */
    List<User> findTeachers();

    /**
     * 查询学生列表
     * @return 学生列表
     */
    List<User> findStudents();

    /**
     * 根据教师ID查询关联的学生列表
     * @param teacherId 教师ID
     * @return 学生列表
     */
    List<User> findStudentsByTeacherId(Long teacherId);

    /**
     * 根据学生ID查询关联的教师列表
     * @param studentId 学生ID
     * @return 教师列表
     */
    List<User> findTeachersByStudentId(Long studentId);

    /**
     * 关联学生和教师
     * @param studentId 学生ID
     * @param teacherId 教师ID
     */
    void relateStudentAndTeacher(Long studentId, Long teacherId);

    /**
     * 解除学生和教师的关联
     * @param studentId 学生ID
     * @param teacherId 教师ID
     */
    void unrelateStudentAndTeacher(Long studentId, Long teacherId);
}
