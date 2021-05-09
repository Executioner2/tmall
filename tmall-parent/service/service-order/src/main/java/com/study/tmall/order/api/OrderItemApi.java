package com.study.tmall.order.api;

import com.study.tmall.order.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-08 20:38
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "订单项接口")
@RestController
@RequestMapping("/api/order/orderItem")
public class OrderItemApi {
    @Resource
    private OrderItemService orderItemService;

    // 根据用户id获取购物车商品数量（内部调用）
    @ApiOperation("根据用户id获取购物车商品数量（内部调用）")
    @GetMapping("/inner/getProductNumber/{userId}")
    public Integer getProductNumberByUserId(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @PathVariable String userId) {

        return orderItemService.getProductNumberByUserId(userId);
    }
}
