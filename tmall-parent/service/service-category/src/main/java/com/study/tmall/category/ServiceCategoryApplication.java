package com.study.tmall.category;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 17:11
 * Versions:1.0.0
 * Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.study")
@MapperScan("com.study.tmall.category.mapper")
@EnableFeignClients(basePackages = "com.study.tmall")
public class ServiceCategoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCategoryApplication.class, args);
    }
}
