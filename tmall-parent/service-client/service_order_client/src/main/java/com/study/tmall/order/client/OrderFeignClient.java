package com.study.tmall.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

}
