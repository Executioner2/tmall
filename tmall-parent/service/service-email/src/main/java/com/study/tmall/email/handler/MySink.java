package com.study.tmall.email.handler;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-13 17:19
 * Versions:1.0.0
 * Description:
 */
public interface MySink {
    String INPUT = "input"; // 标准消息接收通道
    String CODE_RECEIVE = "codeReceive"; // 验证码发送消息接收
    String CODE_DEL_RECEIVE = "codeDelReceive"; // 验证码删除消息接收

    @Input("input")
    SubscribableChannel input();

    @Input("codeReceive")
    SubscribableChannel codeReceive();

    @Input("codeDelReceive")
    SubscribableChannel codeDelReceive();
}
