package com.trae.usermanagement.dto;

import com.trae.usermanagement.enums.UserType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 用户更新请求DTO类
 */
@Data
public class UserUpdateRequest {
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @Size(min = 6, message = "密码长度至少为6位")
    private String password;

    private String name;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    private String phone;

    private UserType userType;

    private String address;
    private String avatar;
    private String department;
    private String position;
    private Boolean isEnabled;
}
