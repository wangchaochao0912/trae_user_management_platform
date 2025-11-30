package com.trae.controller;

import com.trae.entity.ClassInfo;
import com.trae.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 班级信息控制器，处理班级相关的REST API请求
 */
@RestController
@RequestMapping("/api/classes")
@Validated
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    /**
     * 获取所有未删除的班级
     * @return 班级列表
     */
    @GetMapping
    public ResponseEntity<List<ClassInfo>> getAllClasses() {
        List<ClassInfo> classes = classInfoService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    /**
     * 根据ID获取班级信息
     * @param id 班级ID
     * @return 班级信息或404响应
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassInfo> getClassById(@PathVariable Long id) {
        Optional<ClassInfo> classInfo = classInfoService.getClassById(id);
        return classInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 创建新班级
     * @param classInfo 班级信息
     * @return 创建的班级信息或400响应
     */
    @PostMapping
    public ResponseEntity<ClassInfo> createClass(@Valid @RequestBody ClassInfo classInfo) {
        try {
            ClassInfo createdClass = classInfoService.createClass(classInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClass);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 更新班级信息
     * @param id 班级ID
     * @param classInfo 新的班级信息
     * @return 更新后的班级信息或404响应
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassInfo> updateClass(@PathVariable Long id, @Valid @RequestBody ClassInfo classInfo) {
        try {
            ClassInfo updatedClass = classInfoService.updateClass(id, classInfo);
            return ResponseEntity.ok(updatedClass);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 逻辑删除班级
     * @param id 班级ID
     * @return 204无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classInfoService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}