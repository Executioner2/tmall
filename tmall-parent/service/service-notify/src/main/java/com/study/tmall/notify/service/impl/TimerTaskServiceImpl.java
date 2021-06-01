package com.study.tmall.notify.service.impl;

import com.alibaba.fastjson.JSON;
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
    public void payOvertime(TimerTask task) {
        // 因为泛型在通过feign传输会转换为linkedHashMap，所以先把它转换为json字符串，再把json字符串转为对象
        OrderInfo orderInfo = JSON.parseObject(JSON.toJSONString(task.getData()), OrderInfo.class);
        // 远程调用取消订单
        orderFeignClient.cancelOrder(orderInfo);
    }

    /**
     * 评价超时
     * @param task
     */
    @Override
    public void reviewOvertime(TimerTask task) {
        // 因为泛型在通过feign传输会转换为linkedHashMap，所以先把它转换为json字符串，再把json字符串转为对象
        OrderInfo orderInfo = JSON.parseObject(JSON.toJSONString(task.getData()), OrderInfo.class);
        // 远程调用不评价商品，订单交易显示完成
        orderFeignClient.reviewOvertime(orderInfo);
    }
}
