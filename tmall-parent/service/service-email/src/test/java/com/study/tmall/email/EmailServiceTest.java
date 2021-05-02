package com.study.tmall.email;

import com.study.tmall.email.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 21:53
 * Versions:1.0.0
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceEmailApplication.class})
public class EmailServiceTest {
    @Resource
    private EmailService mailService;

    @Test
    public void testSend() {
        String to = "2960333936@qq.com";
        mailService.send(to, "模板邮件", UUID.randomUUID().toString().toUpperCase());
    }
}
