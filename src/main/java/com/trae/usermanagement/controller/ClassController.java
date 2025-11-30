package com.trae.usermanagement.controller;

import com.trae.usermanagement.dto.ClassCreateRequest;
import com.trae.usermanagement.dto.ClassDTO;
import com.trae.usermanagement.dto.ClassUpdateRequest;
import com.trae.usermanagement.service.ClassService;
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

/**
 * 班级控制器
 */
@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@Validated
public class ClassController {

    private final ClassService classService;

    /**
     * 创建班级
     */
    @PostMapping
    public ResponseEntity<ClassDTO> createClass(@Valid @RequestBody ClassCreateRequest request) {
        ClassDTO classDTO = classService.createClass(request);
        return new ResponseEntity<>(classDTO, HttpStatus.CREATED);
    }

    /**
     * 获取班级信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> getClassById(@PathVariable Long id) {
        ClassDTO classDTO = classService.getClassById(id);
        return ResponseEntity.ok(classDTO);
    }

    /**
     * 更新班级信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClassUpdateRequest request) {
        ClassDTO classDTO = classService.updateClass(id, request);
        return ResponseEntity.ok(classDTO);
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 分页查询所有班级
     */
    @GetMapping
    public ResponseEntity<Page<ClassDTO>> getAllClasses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<ClassDTO> classes = classService.getAllClasses(pageable);
        return ResponseEntity.ok(classes);
    }

    /**
     * 根据班级名称搜索班级
     */
    @GetMapping("/search/name")
    public ResponseEntity<Page<ClassDTO>> searchClassesByName(
            @RequestParam String className,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<ClassDTO> classes = classService.searchClassesByName(className, pageable);
        return ResponseEntity.ok(classes);
    }

    /**
     * 根据年级查询班级
     */
    @GetMapping("/grade/{grade}")
    public ResponseEntity<Page<ClassDTO>> getClassesByGrade(
            @PathVariable String grade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<ClassDTO> classes = classService.getClassesByGrade(grade, pageable);
        return ResponseEntity.ok(classes);
    }

    /**
     * 高级搜索班级
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ClassDTO>> searchClassesByConditions(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String classCode,
            @RequestParam(required = false) String grade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        
        Page<ClassDTO> classes = classService.searchClassesByConditions(className, classCode, grade, pageable);
        return ResponseEntity.ok(classes);
    }

    /**
     * 验证班级代码是否可用
     */
    @GetMapping("/validate/classCode")
    public ResponseEntity<Boolean> validateClassCode(
            @RequestParam String classCode,
            @RequestParam(required = false) Long excludeId) {
        boolean isAvailable = classService.isClassCodeAvailable(classCode, excludeId);
        return ResponseEntity.ok(isAvailable);
    }
}
