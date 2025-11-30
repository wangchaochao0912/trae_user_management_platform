package com.trae.repository;

import com.trae.entity.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 班级信息数据访问接口，继承JpaRepository，提供班级相关的数据库操作
 */
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long> {

    /**
     * 查询所有未删除的班级
     * @return 未删除的班级列表
     */
    @Query("SELECT c FROM ClassInfo c WHERE c.deleted = false")
    List<ClassInfo> findAllActive();

    /**
     * 根据ID查询未删除的班级
     * @param id 班级ID
     * @return 未删除的班级Optional对象
     */
    @Query("SELECT c FROM ClassInfo c WHERE c.id = :id AND c.deleted = false")
    Optional<ClassInfo> findActiveById(@Param("id") Long id);
}
