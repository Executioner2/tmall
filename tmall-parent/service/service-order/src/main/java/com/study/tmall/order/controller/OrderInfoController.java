package com.study.tmall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:09
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台订单管理")
@RequestMapping("/admin/order/orderInfo")
@RestController
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    // 分页条件显示订单
    @ApiOperation(value = "分页条件显示订单")
    @GetMapping("/list/{current}/{limit}")
    public Result findPageOrderInfo(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "orderQueryVo", value = "查询条件vo")
            OrderQueryVo orderQueryVo){

        Page<OrderInfo> page = new Page<>(current, limit);
        IPage<OrderInfo> pageModule = orderInfoService.findPageOrderInfo(page, orderQueryVo);
        return Result.ok(pageModule);
    }

    // 订单发货
    @ApiOperation(value = "订单发货")
    @PutMapping("/deliverGoods/{id}")
    public Result deliverGoods(
            @ApiParam(name = "id", value = "订单id", required = true)
            @PathVariable String id){

        orderInfoService.deliverGoods(id);
        return Result.ok();
    }
}
