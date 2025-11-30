package com.trae.service.impl;

import com.trae.entity.ClassInfo;
import com.trae.entity.StudentClassRelation;
import com.trae.repository.ClassInfoRepository;
import com.trae.repository.StudentClassRelationRepository;
import com.trae.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 班级信息服务实现类，实现ClassInfoService接口的业务逻辑
 */
@Service
@Transactional
public class ClassInfoServiceImpl implements ClassInfoService {

    @Autowired
    private ClassInfoRepository classInfoRepository;

    @Autowired
    private StudentClassRelationRepository studentClassRelationRepository;

    @Override
    public List<ClassInfo> getAllClasses() {
        return classInfoRepository.findAllActive();
    }

    @Override
    public Optional<ClassInfo> getClassById(Long id) {
        return classInfoRepository.findActiveById(id);
    }

    @Override
    public ClassInfo createClass(ClassInfo classInfo) {
        return classInfoRepository.save(classInfo);
    }

    @Override
    public ClassInfo updateClass(Long id, ClassInfo classInfo) {
        Optional<ClassInfo> existingClass = classInfoRepository.findActiveById(id);
        if (!existingClass.isPresent()) {
            throw new IllegalArgumentException("Class not found");
        }
        ClassInfo updatedClass = existingClass.get();
        updatedClass.setName(classInfo.getName());
        updatedClass.setDescription(classInfo.getDescription());
        return classInfoRepository.save(updatedClass);
    }

    @Override
    public void deleteClass(Long id) {
        Optional<ClassInfo> classInfo = classInfoRepository.findActiveById(id);
        if (classInfo.isPresent()) {
            ClassInfo deletedClass = classInfo.get();
            deletedClass.setDeleted(true);
            // 停用该班级所有学生的关联关系
            List<StudentClassRelation> relations = studentClassRelationRepository.findActiveByClassInfo(deletedClass);
            relations.forEach(relation -> relation.setActive(false));
            studentClassRelationRepository.saveAll(relations);
            classInfoRepository.save(deletedClass);
        }
}
}