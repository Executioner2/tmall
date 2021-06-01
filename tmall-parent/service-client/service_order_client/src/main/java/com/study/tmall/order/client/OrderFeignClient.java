package com.study.tmall.order.client;

import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-08 20:36
 * Versions:1.0.0
 * Description:
 */
@Component
@FeignClient("service-order")
public interface OrderFeignClient {
    // 根据用户id获取购物车商品数量（内部调用）
    @GetMapping("/api/order/orderItem/inner/getProductNumber/{userId}")
    Integer getProductNumberByUserId(@PathVariable("userId") String userId);

    // 更新订单项评价状态（内部调用）
    @PostMapping("/api/order/orderItem/inner/update/reviewStatus/{id}/{status}")
    void updateReviewStatus(@PathVariable("id") String id, @PathVariable("status") Integer status);

    // 根据订单项id和用户id查询订单项（内部调用）
    @GetMapping("/api/order/orderItem/inner/getOrderItem/{id}/{userId}")
    OrderItem getOrderItem(@PathVariable("id") String id, @PathVariable("userId") String userId);

    // 取消订单（内部调用）
    @PostMapping("/api/order/orderInfo/inner/cancelOrder")
    void cancelOrder(@RequestBody OrderInfo orderInfo);

    // 超时未评价（内部调用）
    @PostMapping("/api/order/orderInfo/inner/defaultReview")
    void reviewOvertime(@RequestBody OrderInfo orderInfo);
}
