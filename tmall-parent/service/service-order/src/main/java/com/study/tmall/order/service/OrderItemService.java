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

    // 加入到购物车
    OrderItem joinOrderItem(String token, OrderItem orderItem);

    // 获取订单项（包括订单项对应的商品信息）
    List<OrderItem> getOrderItemByToken(String token);

    // 从购物车中移除商品
    void removeProduct(String token, String id);

    // 更新订单项商品数量
    Long updateProductNumber(OrderItem orderItem);

    // 让订单项关联订单id
    void relevanceOrderInfo(List<String> orderItemIdList, String id);

    // 查询出id集合中的订单项和商品
    List<OrderItem> selectByIdList(List<String> orderItemIdList);
}
