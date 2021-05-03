package com.study.tmall.email.listener;

import com.study.tmall.email.service.EmailService;
import com.study.tmall.vo.front.EmailCodeVo;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-02 19:47
 * Versions:1.0.0
 * Description: 邮箱服务mq监听
 */
@Component
@EnableBinding(Sink.class)
public class EmailMqListener {
    @Resource
    private EmailService emailService;

    /**
     * 发送邮件
     */
    @StreamListener(Sink.INPUT)
    public void sendEmail(Message<EmailCodeVo> message) {
        EmailCodeVo emailCodeVo = message.getPayload();
        emailService.sendCode(emailCodeVo);
    }
}
