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
}
