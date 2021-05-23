package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.ArithmeticTypeEnum;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.enums.ReviewEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.order.mapper.OrderItemMapper;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.product.client.ProductFeignClient;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.util.JwtHelper;
import com.study.tmall.vo.after_end.ProductStockVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private OrderInfoService orderInfoService;

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
    public OrderItem joinOrderItem(String token, OrderItem orderItem) {
        String userId = JwtHelper.getUserId(token);

        // 根据userId查询是否有未下单的购物车，且商品id相同，如果相同更新购物车中商品数量即可，不用新增加数据
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("product_id", orderItem.getProductId());
        wrapper.isNull("order_id");
        OrderItem item = baseMapper.selectOne(wrapper);

        // 远程调用service-product模块更新商品数量
        ProductStockVo productStockVo = new ProductStockVo(); // 专门用来更新商品数量的vo
        BeanUtils.copyProperties(orderItem, productStockVo);
        productStockVo.setType(ArithmeticTypeEnum.SUBTRACT);
        Long stock = productFeignClient.updateProductNumber(productStockVo);
        if (stock != null) {
            int flag;
            // 如果购物车中没有该商品则添加数据
            if (item == null) {
                // 添加到数据库
                orderItem.setUserId(userId);
                flag = baseMapper.insert(orderItem);
            } else { // 否则只更新商品数量
                Integer number = orderItem.getNumber();
                BeanUtils.copyProperties(item, orderItem);
                orderItem.setNumber(item.getNumber() + number);
                flag = baseMapper.updateById(orderItem);
            }

            return flag == 1 ? orderItem : null;
        }

        return null;
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
        ProductStockVo productStockVo = new ProductStockVo(); // 专门用来更新商品数量的vo
        BeanUtils.copyProperties(orderItem, productStockVo);
        productStockVo.setType(ArithmeticTypeEnum.ADD);
        Long stock = productFeignClient.updateProductNumber(productStockVo);

        if (stock != null) { // 如果库存更新成功就把商品从购物车中删除
            baseMapper.delete(wrapper);
        }
    }

    /**
     * 更新订单项商品数量
     * @param orderItem
     */
    @Override
    public Long updateProductNumber(OrderItem orderItem) {
       // 先根据订单项id查询订单项原来的商品数量
        String id = orderItem.getId();
        OrderItem oldOrderItem = baseMapper.selectById(id);
        Integer oldNumber = oldOrderItem.getNumber();
        Integer newNumber = orderItem.getNumber();

        // 设置商品数量更新对象
        ProductStockVo productStockVo = new ProductStockVo();
        BeanUtils.copyProperties(orderItem, productStockVo);
        productStockVo.setNumber(Math.abs((oldNumber - newNumber))); // 要更新的数量
        // 进行的算术，是增加还是减少
        if (oldNumber < newNumber) { // 如果旧的数量小于新的数量库存就减去新增的
            productStockVo.setType(ArithmeticTypeEnum.SUBTRACT);
        } else if (oldNumber > newNumber) { // 如果旧的大于新的就是加上减去的
            productStockVo.setType(ArithmeticTypeEnum.ADD);
        } else { // 如果相等了，那么就返回不更新
            return null;
        }

        // 远程调用更新商品库存
        Long stock = productFeignClient.updateProductNumber(productStockVo);
        // 如果库存更新成功，那么订单项中的商品数量也更新
        if (stock != null) {
            baseMapper.updateById(orderItem);
        }
        // 返回库存
        return stock;

    }

    /**
     * 让订单项关联订单id
     * @param orderItemIdList
     * @param id 订单id
     */
    @Override
    public void relevanceOrderInfo(List<String> orderItemIdList, String id) {
        orderItemIdList.stream().forEach(item -> {
            QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
            wrapper.isNull("order_id");
            wrapper.eq("id", item);
            OrderItem orderItem = baseMapper.selectOne(wrapper);
            orderItem.setOrderId(id);
            baseMapper.updateById(orderItem);
        });
    }

    /**
     * 查询出id集合中的订单项和商品
     * @param orderItemIdList
     * @return
     */
    @Override
    public List<OrderItem> selectByIdList(List<String> orderItemIdList) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemIdList.stream().forEach(item -> {
            OrderItem orderItem = baseMapper.selectById(item);
            if (orderItem != null) {
                orderItems.add(orderItem);
            }
        });
        // 远程调用查询商品信息集合
        this.packOrderItems(orderItems);
        return orderItems;
    }

    /**
     * 根据订单id取得订单项信息
     * @param orderIdList
     * @return
     */
    @Override
    public Map<String, List<OrderItem>> getOrderItemByOrderId(List<String> orderIdList) {
        // 根据订单id查询出所有订单项
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.in("order_id", orderIdList);
        List<OrderItem> orderItems = baseMapper.selectList(wrapper);

        // 根据订单id对订单项集合进行分组
        Map<String, List<OrderItem>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
        Iterator<String> it = orderItemMap.keySet().iterator();
        while (it.hasNext()) {
            List<OrderItem> items = orderItemMap.get(it.next());
            // 对订单项进行封装
            this.packOrderItems(items);
        }

        // 返回订单项map集合
        return orderItemMap;
    }

    /**
     * 根据订单id取得订单项信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderItem> getOrderItemByOrderId(String orderId) {
        // 根据订单id查询出所有订单项
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = baseMapper.selectList(wrapper);
        // 对订单项进行封装
        this.packOrderItems(orderItems);
        return orderItems;
    }

    /**
     * 根据订单id删除订单项
     * @param id
     */
    @Override
    public void removeByOrderId(String id) {
       QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
       wrapper.eq("order_id", id);
       baseMapper.delete(wrapper);
    }

    /**
     * 根据订单项id查询订单项和商品
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getOrderItemDetailsById(String id) {
        Map<String, Object> map = new HashMap<>();
        // 先根据订单项id查询出订单项
        OrderItem orderItem = baseMapper.selectById(id);
        if (orderItem == null) throw new TmallException(ResultCodeEnum.PARAM_ERROR);

        // 查询商品信息
        ProductInfo productInfo = productFeignClient.innerGetProductInfoById(orderItem.getProductId());

        map.put("orderItem", orderItem);
        map.put("productInfo", productInfo);
        return map;
    }

    /**
     * 更新订单项评价状态（内部调用）
     * @param id
     * @param status
     */
    @Override
    public void updateReviewStatus(String id, Integer status) {
        OrderItem orderItem = baseMapper.selectById(id);
        if (orderItem == null) throw new TmallException(ResultCodeEnum.PARAM_ERROR);

        // 更新订单项评价状态
        orderItem.setIsReview(status);
        baseMapper.updateById(orderItem);

        // 查询订单项对应的订单内是否所有订单项都完成了评价
        String orderId = orderItem.getOrderId();
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = baseMapper.selectList(wrapper);
        Iterator<OrderItem> iterator = orderItems.iterator();
        // 遍历所有订单项
        while (iterator.hasNext()) {
            OrderItem item = iterator.next();
            // 如果有一个订单项没有评价就返回
            if (item.getIsReview() == ReviewEnum.NOT_REVIEW.getStatus()) {
                return;
            }
        }

        // 执行到这里表示所有订单项都完成了评价，那么更新订单状态为已完成
        orderInfoService.updateOrderStatus(orderId, OrderStatusEnum.COMPLETE_TRANSACTION.getStatus());
    }

    /**
     * 根据订单项id和用户id查询订单项
     * @param id
     * @param userId
     * @return
     */
    @Override
    public OrderItem getOrderItem(String id, String userId) {
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }

}
