package com.study.tmall.email.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-27 18:26
 * Versions:1.0.0
 * Description:
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${spring.mail.username}")
    private String from;

    @Value("${email.project}")
    private String project;

    @Value("${email.author}")
    private String author;

    public static String FROM;
    public static String PROJECT;
    public static String AUTHOR;

    @Override
    public void afterPropertiesSet() throws Exception {
        FROM = from;
        PROJECT = project;
        AUTHOR = author;
    }
}
