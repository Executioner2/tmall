package com.study.tmall.order.api;

import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.order.SettlementVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-18 19:40
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderInfoApi {
    @Resource
    private OrderInfoService orderInfoService;

    // 下单
    @ApiOperation("下单")
    @PostMapping("/auth/settlement")
    public Result settlement(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "settlementVo", value = "结算vo对象", required = true)
            @RequestBody SettlementVo settlementVo) {

        String token = request.getHeader("token");
        String orderId = orderInfoService.settlement(token, settlementVo);
        return Result.ok(orderId);
    }

    // 获取订单信息
    @ApiOperation("获取订单信息")
    @PostMapping("/auth/getOrderInfo/{orderId}")
    public Result getOrderInfo(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String token = request.getHeader("token");
        OrderInfo orderInfo = orderInfoService.getOrderInfo(token, orderId);
        return Result.ok(orderInfo);
    }

    // 根据条件显示用户订单
    @ApiOperation("根据条件显示用户订单")
    @PostMapping("/auth/list/orderInfo/{orderStatus}")
    public Result listOrderInfo(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "orderStatus", value = "订单状态")
            @PathVariable Integer orderStatus) {

        String token = request.getHeader("token");
        List<OrderInfo> orderInfos = orderInfoService.listOrderInfo(token, orderStatus);
        return Result.ok(orderInfos);
    }

    // 用户催卖家发货，卖家光速发货
    @ApiOperation("用户催卖家发货，卖家光速发货")
    @PostMapping("/auth/deliver/{orderId}")
    public Result deliver(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String token = request.getHeader("token");
        orderInfoService.deliverGoodsByUser(token, orderId);
        return Result.ok();
    }

    // 获取订单详细信息
    @ApiOperation("获取订单详细信息")
    @PostMapping("/auth/getOrderInfoDetails/{orderId}")
    public Result getOrderInfoDetails(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String token = request.getHeader("token");
        OrderInfo orderInfo = orderInfoService.getOrderInfoDetails(token, orderId);
        return Result.ok(orderInfo);
    }

    // 确认收货
    @ApiOperation("确认收货")
    @PostMapping("/auth/confirmReceipt/{orderId}")
    public Result confirmReceipt(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable String orderId) {

        String token = request.getHeader("token");
        orderInfoService.confirmReceipt(token, orderId);
        return Result.ok();
    }
}
