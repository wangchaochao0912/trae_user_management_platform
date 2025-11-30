package com.trae.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页响应DTO类
 */
@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;
}