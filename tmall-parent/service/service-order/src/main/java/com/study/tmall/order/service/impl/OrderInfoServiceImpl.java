package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.order.mapper.OrderInfoMapper;
import com.study.tmall.order.service.OrderInfoService;
import org.springframework.stereotype.Service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
}
