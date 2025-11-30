package com.trae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trae.usermanagement"})
public class UserManagementPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementPlatformApplication.class, args);
    }

}