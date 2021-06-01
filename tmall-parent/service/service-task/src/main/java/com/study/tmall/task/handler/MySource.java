package com.study.tmall.task.handler;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:09
 * Versions:1.0.0
 * Description:
 */
public interface MySource {
    String OUTPUT = "output"; // 标准消息发送通道
    String TIMER_PAY_TASK_SEND = "timerPayTaskSend"; // 计时器支付任务通知
    String TIMER_REVIEW_TASK_SEND = "timerReviewTaskSend"; // 计时器支付任务通知

    @Output("output")
    MessageChannel output();

    @Output("timerPayTaskSend")
    MessageChannel timerPayTaskSend();

    @Output("timerReviewTaskSend")
    MessageChannel timerReviewTaskSend();
}
