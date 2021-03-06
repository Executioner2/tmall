package com.study.tmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.order.OrderItem;

import java.util.List;
import java.util.Map;

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

    // 根据订单id取得订单项信息
    Map<String, List<OrderItem>> getOrderItemByOrderId(List<String> orderIdList);

    // 根据订单id取得订单项信息
    List<OrderItem> getOrderItemByOrderId(String orderId);

    // 根据订单id删除订单项
    void removeByOrderId(String id);

    // 根据订单项id查询订单项和商品
    Map<String, Object> getOrderItemDetailsById(String id);

    // 更新订单项评价状态（内部调用）
    void updateReviewStatus(String id, Integer status);

    // 根据订单项id和用户id查询订单项
    OrderItem getOrderItem(String id, String userId);

    // 超时未评价
    void reviewOvertime(String orderId, String userId);
}
