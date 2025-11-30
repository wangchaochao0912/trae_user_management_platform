package com.trae.usermanagement.service.impl;

import com.trae.usermanagement.dto.TeacherStudentRelationRequest;
import com.trae.usermanagement.dto.ClassStudentRelationRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.ClassDTO;
import com.trae.usermanagement.entity.User;
import com.trae.usermanagement.entity.ClassInfo;
import com.trae.usermanagement.entity.TeacherStudentRelation;
import com.trae.usermanagement.entity.ClassStudentRelation;
import com.trae.usermanagement.enums.UserType;
import com.trae.usermanagement.exception.BusinessException;
import com.trae.usermanagement.repository.UserRepository;
import com.trae.usermanagement.repository.ClassInfoRepository;
import com.trae.usermanagement.repository.TeacherStudentRelationRepository;
import com.trae.usermanagement.repository.ClassStudentRelationRepository;
import com.trae.usermanagement.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联关系服务实现类
 */
@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements RelationService {

    private final UserRepository userRepository;
    private final ClassInfoRepository classInfoRepository;
    private final TeacherStudentRelationRepository teacherStudentRelationRepository;
    private final ClassStudentRelationRepository classStudentRelationRepository;

    /**
     * 建立教师学生关联关系，使用READ_COMMITTED隔离级别处理并发问题
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createTeacherStudentRelation(TeacherStudentRelationRequest request) {
        // 验证教师是否存在且类型正确
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new BusinessException("教师不存在", "TEACHER_NOT_FOUND"));
        if (teacher.getUserType() != UserType.TEACHER) {
            throw new BusinessException("指定用户不是教师", "USER_NOT_TEACHER");
        }

        // 验证学生是否存在且类型正确
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new BusinessException("学生不存在", "STUDENT_NOT_FOUND"));
        if (student.getUserType() != UserType.STUDENT) {
            throw new BusinessException("指定用户不是学生", "USER_NOT_STUDENT");
        }

        // 检查关联关系是否已存在
        if (teacherStudentRelationRepository.existsByTeacherIdAndStudentId(request.getTeacherId(), request.getStudentId())) {
            throw new BusinessException("教师学生关联关系已存在", "RELATION_EXISTS");
        }

        // 创建关联关系
        TeacherStudentRelation relation = new TeacherStudentRelation();
        relation.setTeacher(teacher);
        relation.setStudent(student);
        relation.setRelationType(request.getRelationType());
        relation.setNotes(request.getNotes());

        teacherStudentRelationRepository.save(relation);
    }

    /**
     * 建立班级学生关联关系，使用READ_COMMITTED隔离级别处理并发问题
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createClassStudentRelation(ClassStudentRelationRequest request) {
        // 验证班级是否存在
        ClassInfo classInfo = classInfoRepository.findById(request.getClassId())
                .orElseThrow(() -> new BusinessException("班级不存在", "CLASS_NOT_FOUND"));

        // 验证学生是否存在且类型正确
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new BusinessException("学生不存在", "STUDENT_NOT_FOUND"));
        if (student.getUserType() != UserType.STUDENT) {
            throw new BusinessException("指定用户不是学生", "USER_NOT_STUDENT");
        }

        // 检查关联关系是否已存在
        if (classStudentRelationRepository.existsByClassInfoIdAndStudentId(request.getClassId(), request.getStudentId())) {
            throw new BusinessException("班级学生关联关系已存在", "RELATION_EXISTS");
        }

        // 创建关联关系
        ClassStudentRelation relation = new ClassStudentRelation();
        relation.setClassInfo(classInfo);
        relation.setStudent(student);
        relation.setStudentNumber(request.getStudentNumber());
        relation.setSeatNumber(request.getSeatNumber());
        relation.setNotes(request.getNotes());

        classStudentRelationRepository.save(relation);

        // 更新班级学生数量
        classInfoRepository.updateStudentCount(request.getClassId(), 1);
    }

    @Override
    @Transactional
    public void deleteTeacherStudentRelation(Long teacherId, Long studentId) {
        // 检查关联关系是否存在
        if (!teacherStudentRelationRepository.existsByTeacherIdAndStudentId(teacherId, studentId)) {
            throw new BusinessException("教师学生关联关系不存在", "RELATION_NOT_FOUND");
        }

        // 删除关联关系
        teacherStudentRelationRepository.deleteByTeacherIdAndStudentId(teacherId, studentId);
    }

    @Override
    @Transactional
    public void deleteClassStudentRelation(Long classId, Long studentId) {
        // 检查关联关系是否存在
        if (!classStudentRelationRepository.existsByClassInfoIdAndStudentId(classId, studentId)) {
            throw new BusinessException("班级学生关联关系不存在", "RELATION_NOT_FOUND");
        }

        // 删除关联关系
        classStudentRelationRepository.deleteByClassInfoIdAndStudentId(classId, studentId);

        // 更新班级学生数量
        classInfoRepository.updateStudentCount(classId, -1);
    }

    @Override
    public List<UserDTO> getTeacherStudents(Long teacherId) {
        // 验证教师是否存在且类型正确
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException("教师不存在", "TEACHER_NOT_FOUND"));
        if (teacher.getUserType() != UserType.TEACHER) {
            throw new BusinessException("指定用户不是教师", "USER_NOT_TEACHER");
        }

        List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findByTeacherId(teacherId);
        List<UserDTO> students = new ArrayList<>();
        for (TeacherStudentRelation relation : relations) {
            students.add(convertToUserDTO(relation.getStudent()));
        }
        return students;
    }

    @Override
    public List<UserDTO> getStudentTeachers(Long studentId) {
        // 验证学生是否存在且类型正确
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("学生不存在", "STUDENT_NOT_FOUND"));
        if (student.getUserType() != UserType.STUDENT) {
            throw new BusinessException("指定用户不是学生", "USER_NOT_STUDENT");
        }

        List<TeacherStudentRelation> relations = teacherStudentRelationRepository.findByStudentId(studentId);
        List<UserDTO> teachers = new ArrayList<>();
        for (TeacherStudentRelation relation : relations) {
            teachers.add(convertToUserDTO(relation.getTeacher()));
        }
        return teachers;
    }

    @Override
    public List<UserDTO> getClassStudents(Long classId) {
        // 验证班级是否存在
        ClassInfo classInfo = classInfoRepository.findById(classId)
                .orElseThrow(() -> new BusinessException("班级不存在", "CLASS_NOT_FOUND"));

        List<ClassStudentRelation> relations = classStudentRelationRepository.findByClassInfoId(classId);
        List<UserDTO> students = new ArrayList<>();
        for (ClassStudentRelation relation : relations) {
            students.add(convertToUserDTO(relation.getStudent()));
        }
        return students;
    }

    @Override
    public List<ClassDTO> getStudentClasses(Long studentId) {
        // 验证学生是否存在且类型正确
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("学生不存在", "STUDENT_NOT_FOUND"));
        if (student.getUserType() != UserType.STUDENT) {
            throw new BusinessException("指定用户不是学生", "USER_NOT_STUDENT");
        }

        List<ClassStudentRelation> relations = classStudentRelationRepository.findByStudentId(studentId);
        List<ClassDTO> classes = new ArrayList<>();
        for (ClassStudentRelation relation : relations) {
            classes.add(convertToClassDTO(relation.getClassInfo()));
        }
        return classes;
    }

    @Override
    public boolean existsTeacherStudentRelation(Long teacherId, Long studentId) {
        return teacherStudentRelationRepository.existsByTeacherIdAndStudentId(teacherId, studentId);
    }

    @Override
    public boolean existsClassStudentRelation(Long classId, Long studentId) {
        return classStudentRelationRepository.existsByClassInfoIdAndStudentId(classId, studentId);
    }

    /**
     * 将User实体转换为UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    /**
     * 将ClassInfo实体转换为ClassDTO
     */
    private ClassDTO convertToClassDTO(ClassInfo classInfo) {
        ClassDTO dto = new ClassDTO();
        BeanUtils.copyProperties(classInfo, dto);
        return dto;
    }
}
