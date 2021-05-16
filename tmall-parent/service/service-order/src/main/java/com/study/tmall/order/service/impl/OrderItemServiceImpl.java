package com.study.tmall.order.service.impl;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.ArithmeticTypeEnum;
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

        // 封装订单项
        this.packOrderItems(orderItems);

        return orderItems;
    }

    // 封装订单项
    private void packOrderItems(List<OrderItem> orderItems) {
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
        Boolean flag = productFeignClient.updateProductNumber(orderItem, ArithmeticTypeEnum.SUBTRACT);
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

    /**
     * 获取订单项（包括订单项对应的商品信息）
     * @param token
     * @return
     */
    @Override
    public List<OrderItem> getOrderItemByToken(String token) {
        // 根据token拿到userId
        String userId = JwtHelper.getUserId(token);
        // 根据userId查询符合条件的订单项（order_id为空的，即未下单的）
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.isNull("order_id");
        List<OrderItem> orderItems = baseMapper.selectList(wrapper);

        // 封装订单项
        this.packOrderItems(orderItems);

        return orderItems;
    }

    /**
     * 从购物车中移除商品
     * @param token
     * @param id
     */
    @Override
    public void removeProduct(String token, String id) {
        String userId = JwtHelper.getUserId(token);

        // 移除条件，用户id和登录用户id一致，订单id为空，即未下单，id等于前端传入的id
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.isNull("order_id");
        wrapper.eq("id", id);

        // 先查询，更新商品的库存
        OrderItem orderItem = baseMapper.selectOne(wrapper);
        // 更新商品库存，加法
        Boolean flag = productFeignClient.updateProductNumber(orderItem, ArithmeticTypeEnum.ADD);

        if (flag) { // 如果库存更新成功就把商品从购物车中删除
            baseMapper.delete(wrapper);
        }
    }

    /**
     * 更新订单项商品数量
     * @param orderItem
     */
    @Override
    public void updateProductNumber(OrderItem orderItem) {
       // 先根据订单项id查询订单项原来的商品数量
        String id = orderItem.getId();
        OrderItem oldOrderItem = baseMapper.selectById(id);
        Integer oldNumber = oldOrderItem.getNumber();
        Integer newNumber = orderItem.getNumber();
        if (oldNumber < newNumber) { // 如果旧的数量小于新的数量就是加

        } else if (oldNumber > newNumber) { // 如果旧的大于新的就是减

        }
    }

}
