package com.study.tmall.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 20:31
 * Versions:1.0.0
 * Description:
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.study.tmall")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceNotifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceNotifyApplication.class, args);
    }
}
