package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.order.mapper.OrderItemMapper;
import com.study.tmall.order.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:07
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}
