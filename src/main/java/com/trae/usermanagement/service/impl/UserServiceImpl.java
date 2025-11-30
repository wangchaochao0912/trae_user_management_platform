package com.trae.usermanagement.service.impl;

import com.trae.usermanagement.dto.UserCreateRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.UserUpdateRequest;
import com.trae.usermanagement.entity.User;
import com.trae.usermanagement.entity.TeacherStudentRelation;
import com.trae.usermanagement.enums.UserType;
import com.trae.usermanagement.exception.BusinessException;
import com.trae.usermanagement.repository.UserRepository;
import com.trae.usermanagement.repository.TeacherStudentRelationRepository;
import com.trae.usermanagement.repository.ClassStudentRelationRepository;
import com.trae.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeacherStudentRelationRepository teacherStudentRelationRepository;
    private final ClassStudentRelationRepository classStudentRelationRepository;

    /**
     * 创建用户，使用READ_COMMITTED隔离级别处理并发问题
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDTO createUser(UserCreateRequest request) {
        // 检查用户名、邮箱、手机号是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("用户名已存在", "USERNAME_EXISTS");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("邮箱已存在", "EMAIL_EXISTS");
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException("手机号已存在", "PHONE_EXISTS");
        }

        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(request, user);
        // 密码可以在这里进行加密处理，这里简化处理
        // user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 保存用户
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在", "USER_NOT_FOUND"));
        return convertToDTO(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDTO updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在", "USER_NOT_FOUND"));

        // 检查更新的用户名、邮箱、手机号是否与其他用户冲突
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
                throw new BusinessException("用户名已存在", "USERNAME_EXISTS");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new BusinessException("邮箱已存在", "EMAIL_EXISTS");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
                throw new BusinessException("手机号已存在", "PHONE_EXISTS");
            }
            user.setPhone(request.getPhone());
        }

        // 更新其他字段
        if (request.getPassword() != null) {
            // user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPassword(request.getPassword());
        }
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getUserType() != null) {
            user.setUserType(request.getUserType());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getPosition() != null) {
            user.setPosition(request.getPosition());
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    /**
     * 逻辑删除用户，同时删除所有关联关系
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 检查用户是否存在
        if (!userRepository.existsByIdAndIsDeletedFalse(id)) {
            throw new BusinessException("用户不存在", "USER_NOT_FOUND");
        }

        // 逻辑删除用户
        userRepository.softDeleteById(id);

        // 删除用户相关的所有关联关系
        teacherStudentRelationRepository.deleteByTeacherId(id);
        teacherStudentRelationRepository.deleteByStudentId(id);
        classStudentRelationRepository.deleteByStudentId(id);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDTO);
    }

    @Override
    public Page<UserDTO> getUsersByType(UserType userType, Pageable pageable) {
        Page<User> users = userRepository.findByUserType(userType, pageable);
        return users.map(this::convertToDTO);
    }

    @Override
    public Page<UserDTO> searchUsersByName(String name, Pageable pageable) {
        Page<User> users = userRepository.findByNameContaining(name, pageable);
        return users.map(this::convertToDTO);
    }

    @Override
    public Page<UserDTO> searchUsersByConditions(String username, String name, String email, String phone, UserType userType, Pageable pageable) {
        Page<User> users = userRepository.findByConditions(username, name, email, phone, userType, pageable);
        return users.map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> getUserTeachers(Long studentId) {
        // 验证学生是否存在
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("学生不存在", "USER_NOT_FOUND"));

        if (student.getUserType() != UserType.STUDENT) {
            throw new BusinessException("指定用户不是学生", "USER_NOT_STUDENT");
        }

        List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findByStudentId(studentId);
        List<UserDTO> teachers = new ArrayList<>();
        for (TeacherStudentRelation relation : relations) {
            teachers.add(convertToDTO(relation.getTeacher()));
        }
        return teachers;
    }

    @Override
    public List<UserDTO> getUserStudents(Long teacherId) {
        // 验证教师是否存在
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException("教师不存在", "USER_NOT_FOUND"));

        if (teacher.getUserType() != UserType.TEACHER) {
            throw new BusinessException("指定用户不是教师", "USER_NOT_TEACHER");
        }

        List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findByTeacherId(teacherId);
        List<UserDTO> students = new ArrayList<>();
        for (TeacherStudentRelation relation : relations) {
            students.add(convertToDTO(relation.getStudent()));
        }
        return students;
    }

    @Override
    public boolean isUsernameAvailable(String username, Long excludeId) {
        if (excludeId != null) {
            return !userRepository.existsByUsernameAndIdNot(username, excludeId);
        }
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email, Long excludeId) {
        if (excludeId != null) {
            return !userRepository.existsByEmailAndIdNot(email, excludeId);
        }
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public boolean isPhoneAvailable(String phone, Long excludeId) {
        if (excludeId != null) {
            return !userRepository.existsByPhoneAndIdNot(phone, excludeId);
        }
        return userRepository.findByPhone(phone).isEmpty();
    }

    /**
     * 将User实体转换为UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
