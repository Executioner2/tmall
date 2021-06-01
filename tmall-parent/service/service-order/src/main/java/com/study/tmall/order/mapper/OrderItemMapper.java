package com.study.tmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.tmall.model.order.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    // 修改订单项评价状态
    void updateOrderItemIsReview(@Param("status") Integer status, @Param("orderItemIdList") List<String> orderItemIdList);
}
