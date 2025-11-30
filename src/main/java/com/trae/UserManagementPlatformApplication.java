package com.trae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主应用类，启动Spring Boot应用程序
 */
@SpringBootApplication
public class UserManagementPlatformApplication {

    /**
     * 主方法，程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserManagementPlatformApplication.class, args);
    }

}