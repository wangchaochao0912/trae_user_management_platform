package com.trae.usermanagement.util;

import com.trae.usermanagement.dto.ClassResponse;
import com.trae.usermanagement.dto.UserResponse;
import com.trae.usermanagement.entity.ClassInfo;
import com.trae.usermanagement.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import com.trae.usermanagement.dto.PageResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体与DTO转换工具类
 */
public class EntityDtoConverter {

    /**
     * 将User实体转换为UserResponse DTO
     */
    public static UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    /**
     * 将User实体列表转换为UserResponse DTO列表
     */
    public static List<UserResponse> convertToUserResponseList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(EntityDtoConverter::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将ClassInfo实体转换为ClassResponse DTO
     */
    public static ClassResponse convertToClassResponse(ClassInfo classInfo) {
        if (classInfo == null) {
            return null;
        }
        ClassResponse response = new ClassResponse();
        BeanUtils.copyProperties(classInfo, response);
        // 设置班主任姓名（如果需要从关联的User实体获取）
        return response;
    }

    /**
     * 将ClassInfo实体列表转换为ClassResponse DTO列表
     */
    public static List<ClassResponse> convertToClassResponseList(List<ClassInfo> classInfos) {
        if (classInfos == null) {
            return null;
        }
        return classInfos.stream()
                .map(EntityDtoConverter::convertToClassResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将Spring Data JPA的Page转换为自定义的PageResponse
     */
    public static <T, R> PageResponse<R> convertToPageResponse(Page<T> page, List<R> content) {
        return new PageResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1, // 前端页码从1开始
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}