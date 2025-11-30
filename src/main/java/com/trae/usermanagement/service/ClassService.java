package com.trae.usermanagement.service;

import com.trae.usermanagement.dto.ClassCreateRequest;
import com.trae.usermanagement.dto.ClassDTO;
import com.trae.usermanagement.dto.ClassUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 班级服务接口
 */
public interface ClassService {

    /**
     * 创建班级
     */
    ClassDTO createClass(ClassCreateRequest request);

    /**
     * 根据ID获取班级信息
     */
    ClassDTO getClassById(Long id);

    /**
     * 根据ID更新班级信息
     */
    ClassDTO updateClass(Long id, ClassUpdateRequest request);

    /**
     * 根据ID逻辑删除班级
     */
    void deleteClass(Long id);

    /**
     * 分页查询所有班级
     */
    Page<ClassDTO> getAllClasses(Pageable pageable);

    /**
     * 根据班级名称模糊查询班级
     */
    Page<ClassDTO> searchClassesByName(String className, Pageable pageable);

    /**
     * 根据年级查询班级
     */
    Page<ClassDTO> getClassesByGrade(String grade, Pageable pageable);

    /**
     * 根据条件分页查询班级
     */
    Page<ClassDTO> searchClassesByConditions(String className, String classCode, String grade, Pageable pageable);

    /**
     * 验证班级代码是否可用
     */
    boolean isClassCodeAvailable(String classCode, Long excludeId);
}
