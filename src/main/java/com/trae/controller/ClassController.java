package com.trae.controller;

import com.trae.entity.Class;
import com.trae.service.ClassService;
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
 * 班级Controller类
 */
@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * 保存班级
     * @param classEntity 班级信息
     * @return 班级信息
     */
    @PostMapping
    public ResponseEntity<Class> saveClass(@RequestBody Class classEntity) {
        try {
            Class savedClass = classService.saveClass(classEntity);
            return new ResponseEntity<>(savedClass, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更新班级
     * @param classEntity 班级信息
     * @return 班级信息
     */
    @PutMapping
    public ResponseEntity<Class> updateClass(@RequestBody Class classEntity) {
        try {
            Class updatedClass = classService.updateClass(classEntity);
            return new ResponseEntity<>(updatedClass, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID删除班级
     * @param id 班级ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClassById(@PathVariable("id") Long id) {
        try {
            classService.deleteClassById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据ID查询班级
     * @param id 班级ID
     * @return 班级信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Class> findClassById(@PathVariable("id") Long id) {
        Optional<Class> classData = classService.findClassById(id);

        if (classData.isPresent()) {
            return new ResponseEntity<>(classData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 根据班级名称查询班级
     * @param name 班级名称
     * @return 班级信息
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Class> findClassByName(@PathVariable("name") String name) {
        Optional<Class> classData = classService.findClassByName(name);

        if (classData.isPresent()) {
            return new ResponseEntity<>(classData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 查询所有班级
     * @return 班级列表
     */
    @GetMapping
    public ResponseEntity<List<Class>> findAllClasses() {
        try {
            List<Class> classes = classService.findAllClasses();

            if (classes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(classes, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 分页查询班级
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向
     * @return 班级分页列表
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Class>> findClassesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Class> classes = classService.findClassesByPage(pageable);

            return new ResponseEntity<>(classes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据班级名称模糊查询班级列表
     * @param name 班级名称
     * @return 班级列表
     */
    @GetMapping("/name-contains/{name}")
    public ResponseEntity<List<Class>> findClassesByNameContaining(@PathVariable("name") String name) {
        try {
            List<Class> classes = classService.findClassesByNameContaining(name);

            if (classes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(classes, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据班级ID查询关联的学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    @GetMapping("/{classId}/students")
    public ResponseEntity<List<Class>> findStudentsByClassId(@PathVariable("classId") Long classId) {
        try {
            List<Class> students = classService.findStudentsByClassId(classId);

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
     * 根据学生ID查询关联的班级列表
     * @param studentId 学生ID
     * @return 班级列表
     */
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Class>> findClassesByStudentId(@PathVariable("studentId") Long studentId) {
        try {
            List<Class> classes = classService.findClassesByStudentId(studentId);

            if (classes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(classes, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 关联学生和班级
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 响应状态
     */
    @PostMapping("/students/{studentId}/classes/{classId}")
    public ResponseEntity<HttpStatus> relateStudentAndClass(@PathVariable("studentId") Long studentId, @PathVariable("classId") Long classId) {
        try {
            classService.relateStudentAndClass(studentId, classId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 解除学生和班级的关联
     * @param studentId 学生ID
     * @param classId 班级ID
     * @return 响应状态
     */
    @DeleteMapping("/students/{studentId}/classes/{classId}")
    public ResponseEntity<HttpStatus> unrelateStudentAndClass(@PathVariable("studentId") Long studentId, @PathVariable("classId") Long classId) {
        try {
            classService.unrelateStudentAndClass(studentId, classId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
