package com.study.tmall.email.listener;

import com.study.tmall.email.handler.MySink;
import com.study.tmall.email.service.EmailService;
import com.study.tmall.dto.DealNotify;
import com.study.tmall.vo.front.EmailCodeVo;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
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
@EnableBinding(MySink.class)
public class EmailMqListener {
    @Resource
    private EmailService emailService;

    /**
     * 发送邮件
     * @param message
     */
    @StreamListener(MySink.CODE_RECEIVE)
    public void sendEmail(Message<EmailCodeVo> message) {
        EmailCodeVo emailCodeVo = message.getPayload();
        emailService.sendCode(emailCodeVo);
    }

    /**
     * 删除redis中的邮箱验证码
     * @param message
     */
    @StreamListener(MySink.CODE_DEL_RECEIVE)
    public void delCode(Message<String> message) {
        String key = message.getPayload();
        emailService.delCode(key);
    }

    /**
     * 订单状态通知
     * @param message
     */
    @StreamListener(MySink.DEAL_NOTIFY_RECEIVE)
    public void dealNotify(Message<DealNotify> message) {
        DealNotify dealNotify = message.getPayload();
        emailService.dealNotify(dealNotify);
    }
}
