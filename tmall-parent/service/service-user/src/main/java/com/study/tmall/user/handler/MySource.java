package com.study.tmall.user.handler;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-13 17:14
 * Versions:1.0.0
 * Description:
 */
public interface MySource {
    String OUTPUT = "output"; // 标准消息发送通道
    String CODE_SEND = "codeSend"; // 验证码发送消息发送
    String CODE_DEL_SEND = "codeDelSend"; // 验证码删除消息发送

    @Output("output")
    MessageChannel output();

    @Output("codeSend")
    MessageChannel codeSend();

    @Output("codeDelSend")
    MessageChannel codeDelSend();
}
