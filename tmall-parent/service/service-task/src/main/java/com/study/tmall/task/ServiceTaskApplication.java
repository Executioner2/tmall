package com.study.tmall.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:05
 * Versions:1.0.0
 * Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // 排除数据库连接
@EnableDiscoveryClient
public class ServiceTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskApplication.class, args);
    }
}
