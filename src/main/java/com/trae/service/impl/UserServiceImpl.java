package com.trae.service.impl;

import com.trae.entity.User;
import com.trae.entity.StudentTeacherRelation;
import com.trae.enums.UserType;
import com.trae.repository.UserRepository;
import com.trae.repository.StudentTeacherRelationRepository;
import com.trae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户Service实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentTeacherRelationRepository studentTeacherRelationRepository;

    @Override
    public User saveUser(User user) {
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查必填项
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        if (user.getUserType() == null) {
            throw new RuntimeException("用户类型不能为空");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        // 检查用户是否存在
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (!existingUser.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否已被其他用户使用
        Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
        if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查必填项
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        if (user.getUserType() == null) {
            throw new RuntimeException("用户类型不能为空");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        // 检查用户是否存在
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        // 删除用户
        userRepository.deleteById(id);

        // 删除用户相关的关联关系
        studentTeacherRelationRepository.deleteByStudentId(id);
        studentTeacherRelationRepository.deleteByTeacherId(id);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findUsersByPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findUsersByUserType(UserType userType) {
        return userRepository.findByUserType(userType);
    }

    @Override
    public List<User> findUsersByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    @Override
    public List<User> findTeachers() {
        return userRepository.findTeachers();
    }

    @Override
    public List<User> findStudents() {
        return userRepository.findStudents();
    }

    @Override
    public List<User> findStudentsByTeacherId(Long teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId);
    }

    @Override
    public List<User> findTeachersByStudentId(Long studentId) {
        return userRepository.findTeachersByStudentId(studentId);
    }

    @Override
    public void relateStudentAndTeacher(Long studentId, Long teacherId) {
        // 检查学生是否存在
        Optional<User> student = userRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new RuntimeException("学生不存在");
        }

        // 检查教师是否存在
        Optional<User> teacher = userRepository.findById(teacherId);
        if (!teacher.isPresent()) {
            throw new RuntimeException("教师不存在");
        }

        // 检查学生和教师是否已经关联
        StudentTeacherRelation existingRelation = studentTeacherRelationRepository.findByStudentIdAndTeacherId(studentId, teacherId);
        if (existingRelation != null) {
            throw new RuntimeException("学生和教师已经关联");
        }

        // 创建关联关系
        StudentTeacherRelation relation = new StudentTeacherRelation();
        relation.setStudentId(studentId);
        relation.setTeacherId(teacherId);
        studentTeacherRelationRepository.save(relation);
    }

    @Override
    public void unrelateStudentAndTeacher(Long studentId, Long teacherId) {
        // 检查学生和教师是否已经关联
        StudentTeacherRelation existingRelation = studentTeacherRelationRepository.findByStudentIdAndTeacherId(studentId, teacherId);
        if (existingRelation == null) {
            throw new RuntimeException("学生和教师没有关联");
        }

        // 解除关联关系
        studentTeacherRelationRepository.deleteByStudentIdAndTeacherId(studentId, teacherId);
    }
}
