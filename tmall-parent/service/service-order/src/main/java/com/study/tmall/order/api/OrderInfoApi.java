package com.study.tmall.order.api;

import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.order.SettlementVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
}
