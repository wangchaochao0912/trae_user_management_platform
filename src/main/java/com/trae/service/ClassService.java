package com.trae.service;

import com.trae.entity.Class;
import com.trae.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 班级Service接口
 */
public interface ClassService {

    /**
     * 保存班级
     * @param classEntity 班级信息
     * @return 班级信息
     */
    Class saveClass(Class classEntity);

    /**
     * 更新班级
     * @param classEntity 班级信息
     * @return 班级信息
     */
    Class updateClass(Class classEntity);

    /**
     * 根据ID删除班级
     * @param id 班级ID
     */
    void deleteClassById(Long id);

    /**
     * 根据ID查询班级
     * @param id 班级ID
     * @return 班级信息
     */
    Optional<Class> findClassById(Long id);

    /**
     * 根据班级名称查询班级
     * @param name 班级名称
     * @return 班级信息
     */
    Optional<Class> findClassByName(String name);

    /**
     * 查询所有班级
     * @return 班级列表
     */
    List<Class> findAllClasses();

    /**
     * 分页查询班级
     * @param pageable 分页信息
     * @return 班级分页列表
     */
    Page<Class> findClassesByPage(Pageable pageable);

    /**
     * 根据班级名称模糊查询班级列表
     * @param name 班级名称
     * @return 班级列表
     */
    List<Class> findClassesByNameContaining(String name);

    /**
     * 根据班级ID查询关联的学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    List<User> findStudentsByClassId(Long classId);

    /**
     * 根据学生ID查询关联的班级列表
     * @param studentId 学生ID
     * @return 班级列表
     */
    List<Class> findClassesByStudentId(Long studentId);

    /**
     * 关联学生和班级
     * @param studentId 学生ID
     * @param classId 班级ID
     */
    void relateStudentAndClass(Long studentId, Long classId);

    /**
     * 解除学生和班级的关联
     * @param studentId 学生ID
     * @param classId 班级ID
     */
    void unrelateStudentAndClass(Long studentId, Long classId);
}
