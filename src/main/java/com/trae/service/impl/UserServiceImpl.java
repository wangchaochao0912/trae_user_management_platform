package com.trae.service.impl;

import com.trae.entity.User;
import com.trae.entity.ClassInfo;
import com.trae.entity.TeacherStudentRelation;
import com.trae.entity.StudentClassRelation;
import com.trae.repository.UserRepository;
import com.trae.repository.ClassInfoRepository;
import com.trae.repository.TeacherStudentRelationRepository;
import com.trae.repository.StudentClassRelationRepository;
import com.trae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类，实现UserService接口的业务逻辑
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassInfoRepository classInfoRepository;

    @Autowired
    private TeacherStudentRelationRepository teacherStudentRelationRepository;

    @Autowired
    private StudentClassRelationRepository studentClassRelationRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllActive();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findActiveById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findActiveByUsername(username);
    }

    @Override
    public List<User> getUsersByType(User.UserType userType) {
        return userRepository.findActiveByUserType(userType);
    }

    @Override
    public User createUser(User user) {
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findActiveByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        // 验证用户类型
        if (user.getUserType() == null) {
            throw new IllegalArgumentException("User type is required");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findActiveById(id);
        if (!existingUser.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        // 更新用户信息
        User updatedUser = existingUser.get();
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            updatedUser.setPassword(user.getPassword());
        }
        if (user.getUserType() != null) {
            updatedUser.setUserType(user.getUserType());
        }
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findActiveById(id);
        if (user.isPresent()) {
            User deletedUser = user.get();
            deletedUser.setDeleted(true);
            // 停用所有相关的师生关系
            if (deletedUser.getUserType() == User.UserType.TEACHER) {
                List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findActiveByTeacher(deletedUser);
                relations.forEach(relation -> relation.setActive(false));
                teacherStudentRelationRepository.saveAll(relations);
            } else if (deletedUser.getUserType() == User.UserType.STUDENT) {
                List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findActiveByStudent(deletedUser);
                relations.forEach(relation -> relation.setActive(false));
                teacherStudentRelationRepository.saveAll(relations);
                // 停用所有相关的学生班级关系
                List<StudentClassRelation> classRelations = studentClassRelationRepository.findActiveByStudent(deletedUser);
                classRelations.forEach(relation -> relation.setActive(false));
                studentClassRelationRepository.saveAll(classRelations);
            }
            userRepository.save(deletedUser);
        }
    }

    @Override
    public List<TeacherStudentRelation> getStudentsByTeacher(Long teacherId) {
        Optional<User> teacher = userRepository.findActiveById(teacherId);
        if (!teacher.isPresent() || teacher.get().getUserType() != User.UserType.TEACHER) {
            throw new IllegalArgumentException("Invalid teacher ID");
        }
        return teacherStudentRelationRepository.findActiveByTeacher(teacher.get());
    }

    @Override
    public List<TeacherStudentRelation> getTeachersByStudent(Long studentId) {
        Optional<User> student = userRepository.findActiveById(studentId);
        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        return teacherStudentRelationRepository.findActiveByStudent(student.get());
    }

    @Override
    public TeacherStudentRelation assignStudentToTeacher(Long teacherId, Long studentId) {
        Optional<User> teacher = userRepository.findActiveById(teacherId);
        Optional<User> student = userRepository.findActiveById(studentId);

        if (!teacher.isPresent() || teacher.get().getUserType() != User.UserType.TEACHER) {
            throw new IllegalArgumentException("Invalid teacher ID");
        }
        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        // 检查关系是否已存在
        TeacherStudentRelation existingRelation = teacherStudentRelationRepository.findActiveByTeacherAndStudent(teacher.get(), student.get());
        if (existingRelation != null) {
            throw new IllegalArgumentException("Student is already assigned to this teacher");
        }
        // 创建新关系
        TeacherStudentRelation relation = new TeacherStudentRelation();
        relation.setTeacher(teacher.get());
        relation.setStudent(student.get());
        return teacherStudentRelationRepository.save(relation);
    }

    @Override
    public void unassignStudentFromTeacher(Long teacherId, Long studentId) {
        Optional<User> teacher = userRepository.findActiveById(teacherId);
        Optional<User> student = userRepository.findActiveById(studentId);

        if (!teacher.isPresent() || teacher.get().getUserType() != User.UserType.TEACHER) {
            throw new IllegalArgumentException("Invalid teacher ID");
        }
        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        // 查找并停用关系
        TeacherStudentRelation relation = teacherStudentRelationRepository.findActiveByTeacherAndStudent(teacher.get(), student.get());
        if (relation != null) {
            relation.setActive(false);
            teacherStudentRelationRepository.save(relation);
        }
    }

    @Override
    public StudentClassRelation assignStudentToClass(Long studentId, Long classId) {
        Optional<User> student = userRepository.findActiveById(studentId);
        Optional<ClassInfo> classInfo = classInfoRepository.findActiveById(classId);

        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (!classInfo.isPresent()) {
            throw new IllegalArgumentException("Invalid class ID");
        }
        // 检查关系是否已存在
        Optional<StudentClassRelation> existingRelation = studentClassRelationRepository.findActiveByStudentAndClassInfo(student.get(), classInfo.get());
        if (existingRelation.isPresent()) {
            throw new IllegalArgumentException("Student is already assigned to this class");
        }
        // 创建新关系
        StudentClassRelation relation = new StudentClassRelation(student.get(), classInfo.get());
        return studentClassRelationRepository.save(relation);
    }

    @Override
    public void unassignStudentFromClass(Long studentId, Long classId) {
        Optional<User> student = userRepository.findActiveById(studentId);
        Optional<ClassInfo> classInfo = classInfoRepository.findActiveById(classId);

        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        if (!classInfo.isPresent()) {
            throw new IllegalArgumentException("Invalid class ID");
        }
        // 查找并停用关系
        Optional<StudentClassRelation> relation = studentClassRelationRepository.findActiveByStudentAndClassInfo(student.get(), classInfo.get());
        if (relation.isPresent()) {
            StudentClassRelation updatedRelation = relation.get();
            updatedRelation.setActive(false);
            studentClassRelationRepository.save(updatedRelation);
        }
    }

    @Override
    public List<StudentClassRelation> getClassesByStudent(Long studentId) {
        Optional<User> student = userRepository.findActiveById(studentId);
        if (!student.isPresent() || student.get().getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException("Invalid student ID");
        }
        return studentClassRelationRepository.findActiveByStudent(student.get());
    }
}