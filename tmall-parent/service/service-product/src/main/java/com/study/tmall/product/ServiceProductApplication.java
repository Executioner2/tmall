package com.study.tmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 17:10
 * Versions:1.0.0
 * Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.study.tmall")
@MapperScan("com.study.tmall.product.mapper")
@EnableDiscoveryClient
public class ServiceProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }
}
