package com.trae.usermanagement.repository;

import com.trae.usermanagement.entity.User;
import com.trae.usermanagement.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查询用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据用户类型查询用户列表
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);

    /**
     * 根据姓名模糊查询用户列表
     */
    Page<User> findByNameContaining(String name, Pageable pageable);

    /**
     * 检查用户名是否已存在（排除指定ID的用户）
     */
    boolean existsByUsernameAndIdNot(String username, Long id);

    /**
     * 检查邮箱是否已存在（排除指定ID的用户）
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * 检查手机号是否已存在（排除指定ID的用户）
     */
    boolean existsByPhoneAndIdNot(String phone, Long id);

    /**
     * 逻辑删除用户
     */
    @Transactional
    @Modifying
    @Query("update User u set u.isDeleted = true where u.id = :id")
    int softDeleteById(@Param("id") Long id);

    /**
     * 查询用户是否存在且未被删除
     */
    boolean existsByIdAndIsDeletedFalse(Long id);

    /**
     * 根据条件分页查询用户
     */
    @Query("select u from User u where 
            (:username is null or u.username like %:username%) and 
            (:name is null or u.name like %:name%) and 
            (:email is null or u.email like %:email%) and 
            (:phone is null or u.phone like %:phone%) and 
            (:userType is null or u.userType = :userType) and 
            u.isDeleted = false")
    Page<User> findByConditions(
            @Param("username") String username,
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("userType") UserType userType,
            Pageable pageable);
}
