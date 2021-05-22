package com.study.tmall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.vo.order.OrderQueryVo;
import com.study.tmall.vo.order.SettlementVo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
public interface OrderInfoService extends IService<OrderInfo> {
    // 分页条件显示订单
    IPage<OrderInfo> findPageOrderInfo(Page<OrderInfo> page, OrderQueryVo orderQueryVo);

    // 订单发货
    void deliverGoods(String id);

    // 下单
    String settlement(String token, SettlementVo settlementVo);

    // 获取订单信息
    OrderInfo getOrderInfo(String token, String orderId);

    // 根据条件显示用户订单
    List<OrderInfo> listOrderInfo(String token, Integer orderStatus);

    // 用户催卖家发货，卖家光速发货
    void deliverGoodsByUser(String token, String orderId);

    // 获取订单详细信息
    OrderInfo getOrderInfoDetails(String token, String orderId);

    // 确认收货
    void confirmReceipt(String token, String orderId);
}
