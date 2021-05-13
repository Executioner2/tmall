package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.order.mapper.OrderItemMapper;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.product.client.ProductFeignClient;
import com.study.tmall.util.JwtHelper;
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
    private ProductFeignClient productFeignClient;

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
        List<ProductInfo> productInfoList = productFeignClient.listProductInfoById(productInfoIdList);

        // 把商品信息放到 orderItems 中
        for (int i = 0; i < orderItems.size(); i++) {
            orderItems.get(i).getParams().put("productInfo", productInfoList.get(i));
        }

        return orderItems;
    }

    /**
     * 根据用户id获取购物车商品数量（内部调用）
     * @param userId
     * @return
     */
    @Override
    public Integer getProductNumberByUserId(String userId) {

        return baseMapper.getProductNumberByUserId(userId);
    }

    /**
     * 加入到购物车
     * @param token
     * @param orderItem
     */
    @Override
    public Boolean joinOrderItem(String token, OrderItem orderItem) {
        String userId = JwtHelper.getUserId(token);

        // 根据userId查询是否有未下单的购物车，且商品id相同，如果相同更新购物车中商品数量即可，不用新增加数据
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("product_id", orderItem.getProductId());
        wrapper.isNull("order_id");
        OrderItem item = baseMapper.selectOne(wrapper);

        // 远程调用service-product模块更新商品数量
        Boolean flag = productFeignClient.updateProductNumber(orderItem);
        if (flag) {
            int insert;
            // 如果购物车中没有该商品则添加数据
            if (item == null) {
                // 添加到数据库
                orderItem.setUserId(userId);
                insert = baseMapper.insert(orderItem);
            } else { // 否则只更新商品数量
                item.setNumber(item.getNumber() + orderItem.getNumber());
                insert = baseMapper.updateById(item);
            }

            return insert == 1 ? true : false;
        }

        return false;
    }
}
