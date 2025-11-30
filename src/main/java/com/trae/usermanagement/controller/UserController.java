package com.trae.usermanagement.controller;

import com.trae.usermanagement.dto.UserCreateRequest;
import com.trae.usermanagement.dto.UserDTO;
import com.trae.usermanagement.dto.UserUpdateRequest;
import com.trae.usermanagement.enums.UserType;
import com.trae.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO userDTO = userService.createUser(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        UserDTO userDTO = userService.updateUser(id, request);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 分页查询所有用户
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据用户类型查询用户
     */
    @GetMapping("/type/{userType}")
    public ResponseEntity<Page<UserDTO>> getUsersByType(
            @PathVariable UserType userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<UserDTO> users = userService.getUsersByType(userType, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据姓名搜索用户
     */
    @GetMapping("/search/name")
    public ResponseEntity<Page<UserDTO>> searchUsersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<UserDTO> users = userService.searchUsersByName(name, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 高级搜索用户
     */
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsersByConditions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) UserType userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<UserDTO> users = userService.searchUsersByConditions(username, name, email, phone, userType, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * 获取学生的教师列表
     */
    @GetMapping("/{studentId}/teachers")
    public ResponseEntity<List<UserDTO>> getUserTeachers(@PathVariable Long studentId) {
        List<UserDTO> teachers = userService.getUserTeachers(studentId);
        return ResponseEntity.ok(teachers);
    }

    /**
     * 获取教师的学生列表
     */
    @GetMapping("/{teacherId}/students")
    public ResponseEntity<List<UserDTO>> getUserStudents(@PathVariable Long teacherId) {
        List<UserDTO> students = userService.getUserStudents(teacherId);
        return ResponseEntity.ok(students);
    }

    /**
     * 验证用户名是否可用
     */
    @GetMapping("/validate/username")
    public ResponseEntity<Boolean> validateUsername(
            @RequestParam String username,
            @RequestParam(required = false) Long excludeId) {
        boolean isAvailable = userService.isUsernameAvailable(username, excludeId);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * 验证邮箱是否可用
     */
    @GetMapping("/validate/email")
    public ResponseEntity<Boolean> validateEmail(
            @RequestParam String email,
            @RequestParam(required = false) Long excludeId) {
        boolean isAvailable = userService.isEmailAvailable(email, excludeId);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * 验证手机号是否可用
     */
    @GetMapping("/validate/phone")
    public ResponseEntity<Boolean> validatePhone(
            @RequestParam String phone,
            @RequestParam(required = false) Long excludeId) {
        boolean isAvailable = userService.isPhoneAvailable(phone, excludeId);
        return ResponseEntity.ok(isAvailable);
    }
}
