package com.study.tmall.notify.handler;

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
    String DEAL_NOTIFY_RECEIVE = "dealNotifyReceive"; // 订单状态消息接收
    String TIMER_PAY_TASK_RECEIVE = "timerPayTaskReceive"; // 计时器支付任务接收
    String TIMER_REVIEW_TASK_RECEIVE = "timerReviewTaskReceive"; // 计时器支付任务接收

    @Input("input")
    SubscribableChannel input();

    @Input("codeReceive")
    SubscribableChannel codeReceive();

    @Input("codeDelReceive")
    SubscribableChannel codeDelReceive();

    @Input("dealNotifyReceive")
    SubscribableChannel dealNotifyReceive();

    @Input("timerPayTaskReceive")
    SubscribableChannel timerPayTaskReceive();

    @Input("timerReviewTaskReceive")
    SubscribableChannel timerReviewTaskReceive();
}
