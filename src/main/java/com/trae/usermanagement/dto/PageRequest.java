package com.trae.usermanagement.dto;

import lombok.Data;

/**
 * 分页请求DTO类
 */
@Data
public class PageRequest {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String sortBy = "id";
    private String sortOrder = "asc";
}