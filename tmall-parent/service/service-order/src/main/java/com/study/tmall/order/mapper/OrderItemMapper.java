package com.study.tmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.tmall.model.order.OrderItem;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:04
 * Versions:1.0.0
 * Description:
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    // 根据用户id获取购物车商品数量（内部调用）
    Integer getProductNumberByUserId(String userId);
}
