package com.study.tmall.order.api;

import com.study.tmall.model.order.OrderItem;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    // 加入到购物车
    @ApiOperation("加入到购物车")
    @PostMapping("/auth/join/orderItem")
    public Result joinOrderItem(
            @ApiParam(name = "orderItem", value = "订单项", required = true)
            @RequestBody OrderItem orderItem,

            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {

        String token = request.getHeader("token");
        OrderItem resultModule = orderItemService.joinOrderItem(token, orderItem);
        return Result.ok(resultModule);
    }

    // 获取订单项（包括订单项对应的商品信息）
    @ApiOperation("获取订单项（包括订单项对应的商品信息）")
    @PostMapping("/auth/getOrderItem")
    public Result getOrderItemByToken(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {

        String token = request.getHeader("token");
        List<OrderItem> orderItems = orderItemService.getOrderItemByToken(token);
        return Result.ok(orderItems);
    }

    // 从购物车中移除商品
    @ApiOperation("从购物车中移除商品")
    @PostMapping("/auth/shopping/remove/product/{id}")
    public Result removeProduct(
            @ApiParam(name = "id", value = "订单项id", required = true)
            @PathVariable String id,

            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {

        String token = request.getHeader("token");
        orderItemService.removeProduct(token, id);
        return Result.ok();
    }

    // 更新订单项商品数量
    @ApiOperation("更新订单项商品数量")
    @PostMapping("/auth/shopping/update/number")
    public Result updateProductNumber(
            @ApiParam(name = "orderItem", value = "订单项", required = true)
            @RequestBody OrderItem orderItem) {

        Long stock = orderItemService.updateProductNumber(orderItem);
        return Result.ok(stock);
    }

    // 根据订单项id查询订单项和商品
    @ApiOperation("根据订单项id查询订单项和商品")
    @GetMapping("/auth/getOrderItemDetailsById/{id}")
    public Result getOrderItemDetailsById(
            @ApiParam(name = "id", value = "订单项id", required = true)
            @PathVariable String id) {

        Map<String, Object> resultMap = orderItemService.getOrderItemDetailsById(id);
        return Result.ok(resultMap);
    }

    // 更新订单项评价状态（内部调用）
    @ApiOperation("更新订单项评价状态（内部调用）")
    @PostMapping("inner/update/reviewStatus/{id}/{status}")
    public void updateReviewStatus(
            @ApiParam(name = "id", value = "订单id", required = true)
            @PathVariable String id,

            @ApiParam(name = "status", value = "评价状态", required = true)
            @PathVariable Integer status) {

        orderItemService.updateReviewStatus(id, status);
    }

    // 根据订单项id和用户id查询订单项（内部调用）
    @ApiOperation("根据订单项id和用户id查询订单项（内部调用）")
    @GetMapping("/inner/getOrderItem/{id}/{userId}")
    public OrderItem getOrderItem(
            @ApiParam(name = "id", value = "订单id", required = true)
            @PathVariable String id,

            @ApiParam(name = "userId", value = "用户id", required = true)
            @PathVariable String userId) {

        return orderItemService.getOrderItem(id, userId);
    }

}
