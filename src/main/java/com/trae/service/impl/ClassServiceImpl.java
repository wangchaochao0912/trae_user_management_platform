package com.trae.service.impl;

import com.trae.entity.Class;
import com.trae.entity.User;
import com.trae.entity.StudentClassRelation;
import com.trae.repository.ClassRepository;
import com.trae.repository.StudentClassRelationRepository;
import com.trae.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 班级Service实现类
 */
@Service
@Transactional
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentClassRelationRepository studentClassRelationRepository;

    @Override
    public Class saveClass(Class classEntity) {
        // 检查班级名称是否已存在
        Optional<Class> existingClass = classRepository.findByName(classEntity.getName());
        if (existingClass.isPresent()) {
            throw new RuntimeException("班级名称已存在");
        }

        // 检查必填项
        if (classEntity.getName() == null || classEntity.getName().isEmpty()) {
            throw new RuntimeException("班级名称不能为空");
        }

        return classRepository.save(classEntity);
    }

    @Override
    public Class updateClass(Class classEntity) {
        // 检查班级是否存在
        Optional<Class> existingClass = classRepository.findById(classEntity.getId());
        if (!existingClass.isPresent()) {
            throw new RuntimeException("班级不存在");
        }

        // 检查班级名称是否已被其他班级使用
        Optional<Class> classWithSameName = classRepository.findByName(classEntity.getName());
        if (classWithSameName.isPresent() && !classWithSameName.get().getId().equals(classEntity.getId())) {
            throw new RuntimeException("班级名称已存在");
        }

        // 检查必填项
        if (classEntity.getName() == null || classEntity.getName().isEmpty()) {
            throw new RuntimeException("班级名称不能为空");
        }

        return classRepository.save(classEntity);
    }

    @Override
    public void deleteClassById(Long id) {
        // 检查班级是否存在
        Optional<Class> existingClass = classRepository.findById(id);
        if (!existingClass.isPresent()) {
            throw new RuntimeException("班级不存在");
        }

        // 删除班级
        classRepository.deleteById(id);

        // 删除班级相关的关联关系
        List<StudentClassRelation> relations = studentClassRelationRepository.findByClassId(id);
        studentClassRelationRepository.deleteAll(relations);
    }

    @Override
    public Optional<Class> findClassById(Long id) {
        return classRepository.findById(id);
    }

    @Override
    public Optional<Class> findClassByName(String name) {
        return classRepository.findByName(name);
    }

    @Override
    public List<Class> findAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public Page<Class> findClassesByPage(Pageable pageable) {
        return classRepository.findAll(pageable);
    }

    @Override
    public List<Class> findClassesByNameContaining(String name) {
        return classRepository.findByNameContaining(name);
    }

    @Override
    public List<User> findStudentsByClassId(Long classId) {
        return classRepository.findStudentsByClassId(classId);
    }

    @Override
    public List<Class> findClassesByStudentId(Long studentId) {
        return classRepository.findClassesByStudentId(studentId);
    }

    @Override
    public void relateStudentAndClass(Long studentId, Long classId) {
        // 检查学生是否存在
        // 这里可以调用UserService的方法来检查学生是否存在
        // 为了简化代码，这里暂时不实现

        // 检查班级是否存在
        Optional<Class> classEntity = classRepository.findById(classId);
        if (!classEntity.isPresent()) {
            throw new RuntimeException("班级不存在");
        }

        // 检查学生和班级是否已经关联
        StudentClassRelation existingRelation = studentClassRelationRepository.findByStudentIdAndClassId(studentId, classId);
        if (existingRelation != null) {
            throw new RuntimeException("学生和班级已经关联");
        }

        // 创建关联关系
        StudentClassRelation relation = new StudentClassRelation();
        relation.setStudentId(studentId);
        relation.setClassId(classId);
        studentClassRelationRepository.save(relation);
    }

    @Override
    public void unrelateStudentAndClass(Long studentId, Long classId) {
        // 检查学生和班级是否已经关联
        StudentClassRelation existingRelation = studentClassRelationRepository.findByStudentIdAndClassId(studentId, classId);
        if (existingRelation == null) {
            throw new RuntimeException("学生和班级没有关联");
        }

        // 解除关联关系
        studentClassRelationRepository.deleteByStudentIdAndClassId(studentId, classId);
    }
}
