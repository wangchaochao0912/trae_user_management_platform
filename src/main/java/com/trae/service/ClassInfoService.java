package com.trae.service;

import com.trae.entity.ClassInfo;

import java.util.List;
import java.util.Optional;

/**
 * 班级信息服务接口，定义班级相关的业务逻辑方法
 */
public interface ClassInfoService {

    /**
     * 获取所有未删除的班级
     * @return 未删除的班级列表
     */
    List<ClassInfo> getAllClasses();

    /**
     * 根据ID获取未删除的班级
     * @param id 班级ID
     * @return 未删除的班级Optional对象
     */
    Optional<ClassInfo> getClassById(Long id);

    /**
     * 创建新班级
     * @param classInfo 班级对象
     * @return 创建后的班级对象
     * @throws IllegalArgumentException 如果班级名称为空
     */
    ClassInfo createClass(ClassInfo classInfo);

    /**
     * 更新班级信息
     * @param id 班级ID
     * @param classInfo 更新后的班级对象
     * @return 更新后的班级对象
     * @throws IllegalArgumentException 如果班级不存在
     */
    ClassInfo updateClass(Long id, ClassInfo classInfo);

    /**
     * 逻辑删除班级
     * @param id 班级ID
     */
    void deleteClass(Long id);
}
