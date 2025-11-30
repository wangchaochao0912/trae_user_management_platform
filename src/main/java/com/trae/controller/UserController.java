package com.trae.controller;

import com.trae.entity.User;
import com.trae.enums.UserType;
import com.trae.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 用户Controller类
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户
     * @param user 用户信息
     * @return 用户信息
     */
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新用户
     * @param user 用户信息
     * @return 用户信息
     */
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        Optional<User> userData = userService.findUserById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable("username") String username) {
        Optional<User> userData = userService.findUserByUsername(username);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        try {
            List<User> users = userService.findAllUsers();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向
     * @return 用户分页列表
     */
    @GetMapping("/page")
    public ResponseEntity<Page<User>> findUsersByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<User> users = userService.findUsersByPage(pageable);

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据用户类型查询用户列表
     * @param userType 用户类型
     * @return 用户列表
     */
    @GetMapping("/user-type/{userType}")
    public ResponseEntity<List<User>> findUsersByUserType(@PathVariable("userType") String userType) {
        try {
            List<User> users = userService.findUsersByUserType(userType);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据姓名模糊查询用户列表
     * @param name 姓名
     * @return 用户列表
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> findUsersByNameContaining(@PathVariable("name") String name) {
        try {
            List<User> users = userService.findUsersByNameContaining(name);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询教师列表
     * @return 教师列表
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<User>> findTeachers() {
        try {
            List<User> teachers = userService.findTeachers();

            if (teachers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(teachers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询学生列表
     * @return 学生列表
     */
    @GetMapping("/students")
    public ResponseEntity<List<User>> findStudents() {
        try {
            List<User> students = userService.findStudents();

            if (students.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(students, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据教师ID查询关联的学生列表
     * @param teacherId 教师ID
     * @return 学生列表
     */
    @GetMapping("/teachers/{teacherId}/students")
    public ResponseEntity<List<User>> findStudentsByTeacherId(@PathVariable("teacherId") Long teacherId) {
        try {
            List<User> students = userService.findStudentsByTeacherId(teacherId);

            if (students.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(students, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据学生ID查询关联的教师列表
     * @param studentId 学生ID
     * @return 教师列表
     */
    @GetMapping("/students/{studentId}/teachers")
    public ResponseEntity<List<User>> findTeachersByStudentId(@PathVariable("studentId") Long studentId) {
        try {
            List<User> teachers = userService.findTeachersByStudentId(studentId);

            if (teachers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(teachers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 关联学生和教师
     * @param studentId 学生ID
     * @param teacherId 教师ID
     * @return 响应状态
     */
    @PostMapping("/students/{studentId}/teachers/{teacherId}")
    public ResponseEntity<HttpStatus> relateStudentAndTeacher(@PathVariable("studentId") Long studentId, @PathVariable("teacherId") Long teacherId) {
        try {
            userService.relateStudentAndTeacher(studentId, teacherId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 解除学生和教师的关联
     * @param studentId 学生ID
     * @param teacherId 教师ID
     * @return 响应状态
     */
    @DeleteMapping("/students/{studentId}/teachers/{teacherId}")
    public ResponseEntity<HttpStatus> unrelateStudentAndTeacher(@PathVariable("studentId") Long studentId, @PathVariable("teacherId") Long teacherId) {
        try {
            userService.unrelateStudentAndTeacher(studentId, teacherId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
