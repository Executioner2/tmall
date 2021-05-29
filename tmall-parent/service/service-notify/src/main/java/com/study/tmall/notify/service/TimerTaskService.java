package com.study.tmall.notify.service;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.model.order.OrderInfo;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 15:20
 * Versions:1.0.0
 * Description:
 */
public interface TimerTaskService {

    // 支付超时
    void payOvertime(TimerTask<OrderInfo> task);
}
