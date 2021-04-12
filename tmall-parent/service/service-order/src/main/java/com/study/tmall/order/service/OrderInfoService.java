package com.study.tmall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.vo.order.OrderQueryVo;

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
}
