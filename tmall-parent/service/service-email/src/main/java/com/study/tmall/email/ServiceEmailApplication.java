package com.study.tmall.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 20:31
 * Versions:1.0.0
 * Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceEmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEmailApplication.class, args);
    }
}
