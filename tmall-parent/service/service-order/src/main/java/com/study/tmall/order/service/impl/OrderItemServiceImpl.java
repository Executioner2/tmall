package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.order.mapper.OrderItemMapper;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.product.client.ProductInfoFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:07
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
    @Resource
    private ProductInfoFeignClient productInfoFeignClient;

    /**
     * 显示订单项
     * @param orderId
     * @return
     */
    @Override
    public List<OrderItem> showByOrderId(String orderId) {
        // 根据订单id查询出订单项
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = baseMapper.selectList(wrapper);

        // 根据订单项中的商品id查询出产品，用远程调用访问product模块
        List<String> productInfoIdList = new ArrayList<>();
        orderItems.stream().forEach(item -> {
            productInfoIdList.add(item.getProductId());
        });

        // 传入list集合是为了减少远程调用，提高处理速度
        List<ProductInfo> productInfoList = productInfoFeignClient.listProductInfoById(productInfoIdList);

        // 把商品信息放到 orderItems 中
        for (int i = 0; i < orderItems.size(); i++) {
            orderItems.get(i).getParams().put("productInfo", productInfoList.get(i));
        }

        return orderItems;
    }
}
