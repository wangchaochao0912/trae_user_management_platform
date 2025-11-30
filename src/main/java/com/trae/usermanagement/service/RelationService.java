package com.trae.usermanagement.service;

import com.trae.usermanagement.dto.TeacherStudentRelationRequest;
import com.trae.usermanagement.dto.ClassStudentRelationRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.ClassDTO;

import java.util.List;

/**
 * 关联关系服务接口
 */
public interface RelationService {

    /**
     * 建立教师学生关联关系
     */
    void createTeacherStudentRelation(TeacherStudentRelationRequest request);

    /**
     * 建立班级学生关联关系
     */
    void createClassStudentRelation(ClassStudentRelationRequest request);

    /**
     * 删除教师学生关联关系
     */
    void deleteTeacherStudentRelation(Long teacherId, Long studentId);

    /**
     * 删除班级学生关联关系
     */
    void deleteClassStudentRelation(Long classId, Long studentId);

    /**
     * 获取教师关联的所有学生
     */
    List<UserDTO> getTeacherStudents(Long teacherId);

    /**
     * 获取学生关联的所有教师
     */
    List<UserDTO> getStudentTeachers(Long studentId);

    /**
     * 获取班级关联的所有学生
     */
    List<UserDTO> getClassStudents(Long classId);

    /**
     * 获取学生关联的所有班级
     */
    List<ClassDTO> getStudentClasses(Long studentId);

    /**
     * 检查教师学生关联关系是否存在
     */
    boolean existsTeacherStudentRelation(Long teacherId, Long studentId);

    /**
     * 检查班级学生关联关系是否存在
     */
    boolean existsClassStudentRelation(Long classId, Long studentId);
}
