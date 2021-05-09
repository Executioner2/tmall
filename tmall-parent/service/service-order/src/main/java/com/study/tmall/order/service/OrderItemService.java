package com.study.tmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.order.OrderItem;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
public interface OrderItemService extends IService<OrderItem> {
    // 显示订单项
    List<OrderItem> showByOrderId(String orderId);

    // 根据用户id获取购物车商品数量（内部调用）
    Integer getProductNumberByUserId(String userId);
}
