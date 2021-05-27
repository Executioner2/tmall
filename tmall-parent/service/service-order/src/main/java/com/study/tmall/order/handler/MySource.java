package com.study.tmall.order.handler;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-27 17:29
 * Versions:1.0.0
 * Description:
 */
public interface MySource {
    String OUTPUT = "output"; // 标准消息发送通道
    String DEAL_NOTIFY_SEND = "dealNotifySend"; // 订单状态通知

    @Output("output")
    MessageChannel output();

    @Output("dealNotifySend")
    MessageChannel dealNotifySend();
}
