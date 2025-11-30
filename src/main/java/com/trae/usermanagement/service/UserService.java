package com.trae.usermanagement.service;

import com.trae.usermanagement.dto.UserCreateRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.UserUpdateRequest;
import com.trae.usermanagement.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 创建用户
     */
    UserDTO createUser(UserCreateRequest request);

    /**
     * 根据ID获取用户信息
     */
    UserDTO getUserById(Long id);

    /**
     * 根据ID更新用户信息
     */
    UserDTO updateUser(Long id, UserUpdateRequest request);

    /**
     * 根据ID逻辑删除用户
     */
    void deleteUser(Long id);

    /**
     * 分页查询所有用户
     */
    Page<UserDTO> getAllUsers(Pageable pageable);

    /**
     * 根据用户类型分页查询用户
     */
    Page<UserDTO> getUsersByType(UserType userType, Pageable pageable);

    /**
     * 根据姓名模糊查询用户
     */
    Page<UserDTO> searchUsersByName(String name, Pageable pageable);

    /**
     * 根据条件分页查询用户
     */
    Page<UserDTO> searchUsersByConditions(String username, String name, String email, String phone, UserType userType, Pageable pageable);

    /**
     * 获取用户关联的教师列表
     */
    List<UserDTO> getUserTeachers(Long studentId);

    /**
     * 获取用户关联的学生列表
     */
    List<UserDTO> getUserStudents(Long teacherId);

    /**
     * 验证用户名是否可用
     */
    boolean isUsernameAvailable(String username, Long excludeId);

    /**
     * 验证邮箱是否可用
     */
    boolean isEmailAvailable(String email, Long excludeId);

    /**
     * 验证手机号是否可用
     */
    boolean isPhoneAvailable(String phone, Long excludeId);
}
