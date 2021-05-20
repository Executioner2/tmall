package com.study.tmall.order.api;

import com.study.tmall.order.service.WeChatService;
import com.study.tmall.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-18 20:15
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "微信支付api")
@RestController
@RequestMapping("/api/order/weChat")
public class WeChatApi {
    @Resource
    private WeChatService weChatService;

    // 生成微信支付二维码
    @ApiOperation("生成微信支付二维码")
    @PostMapping("/auth/createNative/{orderId}")
    public Result createNative(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        Map map = weChatService.createNative(orderId);
        return Result.ok(map);
    }

    // 支付结果轮询
    @ApiOperation("支付结果轮询")
    @PostMapping("/auth/payStatus/poll/{orderId}")
    public Result payStatusPoll(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String msg = weChatService.payStatusPoll(orderId);
        return Result.ok().message(msg);
    }

    // 点击支付按钮直接完成支付功能
    @ApiOperation("点击支付按钮直接完成支付功能")
    @PostMapping("/auth/pay/{orderId}")
    public Result pay (
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String msg = weChatService.pay(orderId);
        return Result.ok().message(msg);
    }
}
