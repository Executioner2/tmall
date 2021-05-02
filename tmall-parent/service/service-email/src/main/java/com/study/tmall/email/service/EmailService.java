package com.study.tmall.email.service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 21:36
 * Versions:1.0.0
 * Description:
 */
public interface EmailService {
    void send(String to, String subject, String text);
}
