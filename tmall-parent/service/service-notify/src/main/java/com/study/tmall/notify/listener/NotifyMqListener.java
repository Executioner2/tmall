package com.study.tmall.notify.listener;

import com.alibaba.fastjson.JSON;
import com.study.tmall.dto.DealNotify;
import com.study.tmall.dto.TimerTask;
import com.study.tmall.enums.TaskTypeEnum;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.notify.handler.MySink;
import com.study.tmall.notify.service.EmailService;
import com.study.tmall.notify.service.TimerTaskService;
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
public class NotifyMqListener {
    @Resource
    private EmailService emailService;
    @Resource
    private TimerTaskService timerTaskService;

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

    /**
     * 计时器支付任务
     * @param message
     */
    @StreamListener(MySink.TIMER_PAY_TASK_RECEIVE)
    public void timerPayTask(Message<TimerTask> message) {
        TimerTask task = message.getPayload();
        if (task.getType() == TaskTypeEnum.PAY_OVERTIME) {
            // 是支付超时
            timerTaskService.payOvertime(task);
        }
    }

    /**
     * 计时器评价任务
     * @param message
     */
    @StreamListener(MySink.TIMER_REVIEW_TASK_RECEIVE)
    public void timerReviewTask(Message<TimerTask> message) {
        System.out.println("计时器评价任务通知接收到了");
        TimerTask task = message.getPayload();
        timerTaskService.reviewOvertime(task);
    }
}
