package com.study.tmall.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
