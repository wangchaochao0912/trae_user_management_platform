package com.trae.usermanagement.util;

import com.trae.usermanagement.dto.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 将自定义的PageRequest转换为Spring Data JPA的Pageable
     */
    public static Pageable convertToPageable(PageRequest pageRequest) {
        if (pageRequest == null) {
            return org.springframework.data.domain.PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        }

        // 处理页码（前端从1开始，Spring Data从0开始）
        Integer page = pageRequest.getPage();
        Integer pageSize = pageRequest.getPageSize();
        int pageNumber = (page != null) ? Math.max(0, page - 1) : 0;
        int size = (pageSize != null) ? Math.max(1, Math.min(100, pageSize)) : 10; // 限制最大页面大小为100

        // 处理排序
        String sortOrder = pageRequest.getSortOrder();
        Sort.Direction direction = (sortOrder != null && "desc".equalsIgnoreCase(sortOrder)) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        String sortByField = pageRequest.getSortBy();
        String sortBy = (sortByField != null) ? sortByField : "id";

        return org.springframework.data.domain.PageRequest.of(pageNumber, size, Sort.by(direction, sortBy));
    }
}