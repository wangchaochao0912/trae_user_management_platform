package com.trae.controller;

import com.trae.entity.User;
import com.trae.entity.TeacherStudentRelation;
import com.trae.entity.StudentClassRelation;
import com.trae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 用户控制器，处理用户相关的REST API请求
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有未删除的用户
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息或404响应
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息或404响应
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据用户类型获取用户列表
     * @param userType 用户类型
     * @return 用户列表
     */
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable User.UserType userType) {
        List<User> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    /**
     * 创建新用户
     * @param user 用户信息
     * @return 创建的用户信息或400响应
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 新的用户信息
     * @return 更新后的用户信息或404响应
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 逻辑删除用户
     * @param id 用户ID
     * @return 204无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取指定教师的所有学生
     * @param teacherId 教师ID
     * @return 师生关系列表或400响应
     */
    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<List<TeacherStudentRelation>> getStudentsByTeacher(@PathVariable Long teacherId) {
        try {
            List<TeacherStudentRelation> relations = userService.getStudentsByTeacher(teacherId);
            return ResponseEntity.ok(relations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 获取指定学生的所有教师
     * @param studentId 学生ID
     * @return 师生关系列表或400响应
     */
    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<List<TeacherStudentRelation>> getTeachersByStudent(@PathVariable Long studentId) {
        try {
            List<TeacherStudentRelation> relations = userService.getTeachersByStudent(studentId);
            return ResponseEntity.ok(relations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 分配学生给教师
     * @param teacherId 教师ID
     * @param studentId 学生ID
     * @return 新创建的师生关系或400响应
     */
    @PostMapping("/teacher/{teacherId}/assign/{studentId}")
    public ResponseEntity<TeacherStudentRelation> assignStudentToTeacher(@PathVariable Long teacherId, @PathVariable Long studentId) {
        try {
            TeacherStudentRelation relation = userService.assignStudentToTeacher(teacherId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(relation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 解除学生与教师的关联
     * @param teacherId 教师ID
     * @param studentId 学生ID
     * @return 204无内容响应或400响应
     */
    @DeleteMapping("/teacher/{teacherId}/unassign/{studentId}")
    public ResponseEntity<Void> unassignStudentFromTeacher(@PathVariable Long teacherId, @PathVariable Long studentId) {
        try {
            userService.unassignStudentFromTeacher(teacherId, studentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 分配学生到班级
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 新创建的学生班级关系或400响应
     */
    @PostMapping("/students/{studentId}/classes/{classId}")
    public ResponseEntity<StudentClassRelation> assignStudentToClass(@PathVariable Long studentId, @PathVariable Long classId) {
        try {
            StudentClassRelation relation = userService.assignStudentToClass(studentId, classId);
            return ResponseEntity.status(HttpStatus.CREATED).body(relation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 解除学生与班级的关联
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 204无内容响应或400响应
     */
    @DeleteMapping("/students/{studentId}/classes/{classId}")
    public ResponseEntity<Void> unassignStudentFromClass(@PathVariable Long studentId, @PathVariable Long classId) {
        try {
            userService.unassignStudentFromClass(studentId, classId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取学生的所有班级
     * @param studentId 学生ID
     * @return 学生的班级关系列表或400响应
     */
    @GetMapping("/students/{studentId}/classes")
    public ResponseEntity<List<StudentClassRelation>> getClassesByStudent(@PathVariable Long studentId) {
        try {
            List<StudentClassRelation> relations = userService.getClassesByStudent(studentId);
            return ResponseEntity.ok(relations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}