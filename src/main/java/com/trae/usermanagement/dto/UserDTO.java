package com.trae.usermanagement.dto;

import com.trae.usermanagement.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户DTO类，用于API响应
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private UserType userType;
    private String address;
    private String avatar;
    private String department;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
