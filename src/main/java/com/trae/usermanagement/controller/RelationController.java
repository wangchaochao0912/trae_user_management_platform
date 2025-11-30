package com.trae.usermanagement.controller;

import com.trae.usermanagement.dto.TeacherStudentRelationRequest;
import com.trae.usermanagement.dto.ClassStudentRelationRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.ClassDTO;
import com.trae.usermanagement.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 关联关系控制器
 */
@RestController
@RequestMapping("/relations")
@RequiredArgsConstructor
@Validated
public class RelationController {

    private final RelationService relationService;

    /**
     * 建立教师学生关联关系
     */
    @PostMapping("/teacher-student")
    public ResponseEntity<Void> createTeacherStudentRelation(@Valid @RequestBody TeacherStudentRelationRequest request) {
        relationService.createTeacherStudentRelation(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 建立班级学生关联关系
     */
    @PostMapping("/class-student")
    public ResponseEntity<Void> createClassStudentRelation(@Valid @RequestBody ClassStudentRelationRequest request) {
        relationService.createClassStudentRelation(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 删除教师学生关联关系
     */
    @DeleteMapping("/teacher-student")
    public ResponseEntity<Void> deleteTeacherStudentRelation(
            @RequestParam Long teacherId,
            @RequestParam Long studentId) {
        relationService.deleteTeacherStudentRelation(teacherId, studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除班级学生关联关系
     */
    @DeleteMapping("/class-student")
    public ResponseEntity<Void> deleteClassStudentRelation(
            @RequestParam Long classId,
            @RequestParam Long studentId) {
        relationService.deleteClassStudentRelation(classId, studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取教师的学生列表
     */
    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<List<UserDTO>> getTeacherStudents(@PathVariable Long teacherId) {
        List<UserDTO> students = relationService.getTeacherStudents(teacherId);
        return ResponseEntity.ok(students);
    }

    /**
     * 获取学生的教师列表
     */
    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<List<UserDTO>> getStudentTeachers(@PathVariable Long studentId) {
        List<UserDTO> teachers = relationService.getStudentTeachers(studentId);
        return ResponseEntity.ok(teachers);
    }

    /**
     * 获取班级的学生列表
     */
    @GetMapping("/class/{classId}/students")
    public ResponseEntity<List<UserDTO>> getClassStudents(@PathVariable Long classId) {
        List<UserDTO> students = relationService.getClassStudents(classId);
        return ResponseEntity.ok(students);
    }

    /**
     * 获取学生的班级列表
     */
    @GetMapping("/student/{studentId}/classes")
    public ResponseEntity<List<ClassDTO>> getStudentClasses(@PathVariable Long studentId) {
        List<ClassDTO> classes = relationService.getStudentClasses(studentId);
        return ResponseEntity.ok(classes);
    }

    /**
     * 检查教师学生关联关系是否存在
     */
    @GetMapping("/check/teacher-student")
    public ResponseEntity<Boolean> checkTeacherStudentRelation(
            @RequestParam Long teacherId,
            @RequestParam Long studentId) {
        boolean exists = relationService.existsTeacherStudentRelation(teacherId, studentId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 检查班级学生关联关系是否存在
     */
    @GetMapping("/check/class-student")
    public ResponseEntity<Boolean> checkClassStudentRelation(
            @RequestParam Long classId,
            @RequestParam Long studentId) {
        boolean exists = relationService.existsClassStudentRelation(classId, studentId);
        return ResponseEntity.ok(exists);
    }
}
