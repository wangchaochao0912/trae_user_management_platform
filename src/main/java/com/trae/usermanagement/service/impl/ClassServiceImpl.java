package com.trae.usermanagement.service.impl;

import com.trae.usermanagement.dto.ClassCreateRequest;
import com.trae.usermanagement.dto.ClassDTO;
import com.trae.usermanagement.dto.ClassUpdateRequest;
import com.trae.usermanagement.entity.ClassInfo;
import com.trae.usermanagement.exception.BusinessException;
import com.trae.usermanagement.repository.ClassInfoRepository;
import com.trae.usermanagement.repository.ClassStudentRelationRepository;
import com.trae.usermanagement.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 班级服务实现类
 */
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassInfoRepository classInfoRepository;
    private final ClassStudentRelationRepository classStudentRelationRepository;

    /**
     * 创建班级，使用READ_COMMITTED隔离级别处理并发问题
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ClassDTO createClass(ClassCreateRequest request) {
        // 检查班级代码是否已存在
        if (classInfoRepository.findByClassCode(request.getClassCode()).isPresent()) {
            throw new BusinessException("班级代码已存在", "CLASS_CODE_EXISTS");
        }

        // 创建班级实体
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(request, classInfo);
        classInfo.setStudentCount(0); // 初始学生数量为0

        // 保存班级
        ClassInfo savedClass = classInfoRepository.save(classInfo);
        return convertToDTO(savedClass);
    }

    @Override
    public ClassDTO getClassById(Long id) {
        ClassInfo classInfo = classInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("班级不存在", "CLASS_NOT_FOUND"));
        return convertToDTO(classInfo);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ClassDTO updateClass(Long id, ClassUpdateRequest request) {
        ClassInfo classInfo = classInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("班级不存在", "CLASS_NOT_FOUND"));

        // 检查更新的班级代码是否与其他班级冲突
        if (request.getClassCode() != null && !request.getClassCode().equals(classInfo.getClassCode())) {
            if (classInfoRepository.existsByClassCodeAndIdNot(request.getClassCode(), id)) {
                throw new BusinessException("班级代码已存在", "CLASS_CODE_EXISTS");
            }
            classInfo.setClassCode(request.getClassCode());
        }

        // 更新其他字段
        if (request.getClassName() != null) {
            classInfo.setClassName(request.getClassName());
        }
        if (request.getGrade() != null) {
            classInfo.setGrade(request.getGrade());
        }
        if (request.getDescription() != null) {
            classInfo.setDescription(request.getDescription());
        }

        ClassInfo updatedClass = classInfoRepository.save(classInfo);
        return convertToDTO(updatedClass);
    }

    /**
     * 逻辑删除班级，同时删除所有关联关系
     */
    @Override
    @Transactional
    public void deleteClass(Long id) {
        // 检查班级是否存在
        if (!classInfoRepository.existsByIdAndIsDeletedFalse(id)) {
            throw new BusinessException("班级不存在", "CLASS_NOT_FOUND");
        }

        // 逻辑删除班级
        classInfoRepository.softDeleteById(id);

        // 删除班级相关的所有关联关系
        classStudentRelationRepository.deleteByClassInfoId(id);
    }

    @Override
    public Page<ClassDTO> getAllClasses(Pageable pageable) {
        Page<ClassInfo> classes = classInfoRepository.findAll(pageable);
        return classes.map(this::convertToDTO);
    }

    @Override
    public Page<ClassDTO> searchClassesByName(String className, Pageable pageable) {
        Page<ClassInfo> classes = classInfoRepository.findByClassNameContaining(className, pageable);
        return classes.map(this::convertToDTO);
    }

    @Override
    public Page<ClassDTO> getClassesByGrade(String grade, Pageable pageable) {
        Page<ClassInfo> classes = classInfoRepository.findByGrade(grade, pageable);
        return classes.map(this::convertToDTO);
    }

    @Override
    public Page<ClassDTO> searchClassesByConditions(String className, String classCode, String grade, Pageable pageable) {
        Page<ClassInfo> classes = classInfoRepository.findByConditions(className, classCode, grade, pageable);
        return classes.map(this::convertToDTO);
    }

    @Override
    public boolean isClassCodeAvailable(String classCode, Long excludeId) {
        if (excludeId != null) {
            return !classInfoRepository.existsByClassCodeAndIdNot(classCode, excludeId);
        }
        return classInfoRepository.findByClassCode(classCode).isEmpty();
    }

    /**
     * 将ClassInfo实体转换为ClassDTO
     */
    private ClassDTO convertToDTO(ClassInfo classInfo) {
        ClassDTO dto = new ClassDTO();
        BeanUtils.copyProperties(classInfo, dto);
        return dto;
    }
}
