package com.study.tmall.order.service;

import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-18 19:58
 * Versions:1.0.0
 * Description:
 */
public interface WeChatService {
    // 生成微信支付二维码
    Map createNative(String orderId);

    // 支付结果轮询
    String payStatusPoll(String orderId);

    // 点击支付按钮直接完成支付功能
    String pay(String orderId);
}
