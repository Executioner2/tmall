package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.order.mapper.OrderInfoMapper;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.client.UserFeignClient;
import com.study.tmall.vo.order.OrderQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private OrderItemService orderItemService;



    /**
     * 分页条件显示订单
     * @param page
     * @param orderQueryVo
     * @return
     */
    @Override
    public IPage<OrderInfo> findPageOrderInfo(Page<OrderInfo> page, OrderQueryVo orderQueryVo) {
        Integer orderStatus = orderQueryVo.getOrderStatus(); // 订单状态
        String userId = orderQueryVo.getUserId(); // 用户id
        String outTradeNo = orderQueryVo.getOutTradeNo(); // 订单编号
        Date createDateEnd = orderQueryVo.getCreateDateEnd(); // 订单创建日期结束
        Date createDateBegin = orderQueryVo.getCreateDateBegin(); // 订单创建日期开始

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(outTradeNo)) {
            wrapper.eq("out_trade_no", outTradeNo);
        }
        if (createDateBegin != null) {
            wrapper.ge("create_date", createDateBegin);
        }
        if (createDateEnd != null) {
            wrapper.le("create_date", createDateEnd);
        }

        IPage<OrderInfo> orderInfoIPage = baseMapper.selectPage(page, wrapper);
        // 每个订单都要获得用户信息，为了减少远程调用量，把这一页所有订单的用户id封装到list集合中
        List<String> userIdList = new ArrayList<>();
        orderInfoIPage.getRecords().stream().forEach(item -> {
            userIdList.add(item.getUserId());
        });
        // 通过远程调用获得用户信息集合
        List<UserInfo> userInfoList = userFeignClient.listUserInfoOfInner(userIdList);

        // 封装参数
        orderInfoIPage.getRecords().stream().forEach(item -> {
            this.packOrderInfo(item, userInfoList);
        });
        return orderInfoIPage;
    }


    /**
     * 订单发货
     * @param id
     */
    @Override
    public void deliverGoods(String id) {
        OrderInfo orderInfo = baseMapper.selectById(id);
        if (orderInfo == null) { // 如果查不出来就抛出参数异常
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 检查订单是否已经付款，处于待发货状态
        Integer orderStatus = orderInfo.getOrderStatus();
        if (orderStatus != OrderStatusEnum.WAIT_SHIPMENTS.getStatus()) {
            throw new TmallException(ResultCodeEnum.DELIVER_GOODS_FAIL);
        }
        // 更新发货时间，更新订单状态
        orderInfo.setDeliveryDate(new Date());
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_TAKE_GOODS.getStatus());
        baseMapper.updateById(orderInfo);
    }

    /**
     * 封装订单参数
     * @param orderInfo
     */
    private OrderInfo packOrderInfo(OrderInfo orderInfo, List<UserInfo> userInfoList) {
        Integer number = 0; // 商品总数量
        BigDecimal totalMoney = new BigDecimal(0);
        Integer orderStatus = orderInfo.getOrderStatus();
        String statusNameByStatus = OrderStatusEnum.getStatusNameByStatus(orderStatus);
        // 获取订单项信息
        List<OrderItem> orderItems = orderItemService.showByOrderId(orderInfo.getId());
        // 封装总数量 和 总金额
        for (OrderItem obj : orderItems) {
            ProductInfo productInfo = (ProductInfo) obj.getParams().get("productInfo");
            number += obj.getNumber();
            totalMoney = totalMoney.add(productInfo.getPromotePrice());
        }

        // 额外数据封装
        orderInfo.getParams().put("number", number); // 商品总数量
        orderInfo.getParams().put("totalMoney", totalMoney); // 商品总金额
        orderInfo.getParams().put("orderItems", orderItems); // 订单项
        orderInfo.getParams().put("orderStatusStr", statusNameByStatus); // 订单状态中文显示

        // 把用户信息封装进去
        if (orderInfo.getUserId() != null) {
            Iterator<UserInfo> it = userInfoList.iterator();
            while (it.hasNext()) {
                UserInfo userInfo = it.next();
                if (orderInfo.getUserId().equals(userInfo.getId())) {
                    if (!StringUtils.isEmpty(userInfo.getNickName())) { // 如果昵称不为空就把昵称做userName
                        orderInfo.getParams().put("customerName", userInfo.getNickName());
                    } else if (!StringUtils.isEmpty(userInfo.getPhone())) { // 如果手机号不为空就把手机号做userName
                        orderInfo.getParams().put("customerName", userInfo.getPhone());
                    } else { // 否则把用户名做userName
                        orderInfo.getParams().put("customerName", userInfo.getName());
                    }
                    break;
                }
            }
        }
        // TODO 后续根据前端需求继续完善
        return orderInfo;
    }
}
