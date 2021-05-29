package com.study.tmall.notify.service.impl;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.notify.service.TimerTaskService;
import com.study.tmall.order.client.OrderFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 15:22
 * Versions:1.0.0
 * Description:
 */
@Service
public class TimerTaskServiceImpl implements TimerTaskService {
    @Resource
    private OrderFeignClient orderFeignClient;

    /**
     * 支付超时
     * @param task
     */
    @Override
    public void payOvertime(TimerTask<OrderInfo> task) {
        // 远程调用取消订单
        System.out.println("远程调用开始");
        orderFeignClient.cancelOrder(task.getData());
        System.out.println("远程调用结束");
    }
}
