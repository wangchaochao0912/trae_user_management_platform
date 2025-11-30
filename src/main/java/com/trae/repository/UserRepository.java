package com.trae.repository;

import com.trae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口，继承JpaRepository，提供用户相关的数据库操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查询所有未删除的用户
     * @return 未删除的用户列表
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActive();

    /**
     * 根据ID查询未删除的用户
     * @param id 用户ID
     * @return 未删除的用户Optional对象
     */
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findActiveById(@Param("id") Long id);

    /**
     * 根据用户名查询未删除的用户
     * @param username 用户名
     * @return 未删除的用户Optional对象
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleted = false")
    Optional<User> findActiveByUsername(@Param("username") String username);

    /**
     * 根据用户类型查询未删除的用户
     * @param userType 用户类型
     * @return 未删除的用户列表
     */
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.deleted = false")
    List<User> findActiveByUserType(@Param("userType") User.UserType userType);
}
