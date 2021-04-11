package com.study.tmall.order.controller;

import com.study.tmall.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:47
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台订单项管理")
@RequestMapping("/admin/order/orderItem")
@RestController
public class OrderItemController {

    // 显示订单项
    @ApiOperation(value = "显示订单项")
    @GetMapping("/show/{orderId}")
    public Result getOrderItem(
            @ApiParam(value = "orderId", name = "订单的id", required = true)
            @PathVariable Integer orderId){

        // TODO 显示订单项
        return Result.ok();
    }
}

