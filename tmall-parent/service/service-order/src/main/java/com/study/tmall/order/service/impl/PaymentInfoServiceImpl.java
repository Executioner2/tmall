package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.enums.PaymentStatusEnum;
import com.study.tmall.enums.PaymentTypeEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.PaymentInfo;
import com.study.tmall.order.mapper.PaymentInfoMapper;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.PaymentInfoService;
import com.study.tmall.result.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:08
 * Versions:1.0.0
 * Description:
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 保存订单信息到支付信息
     * @param orderInfo
     * @param typeEnum
     */
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, PaymentTypeEnum typeEnum) {
        // 先查询有没有，有就不添加新的订单信息了
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderInfo.getId());
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer > 0) return; // 已经有交易记录了，就返回

        // 封装参数
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentType(typeEnum.getStatus());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        // 内容
        StringBuilder baseSubject = new StringBuilder()
                .append("地址：%s").append("&邮编：%s")
                .append("&收货人：%s").append("&联系电话：%s")
                .append("&用户备注：%s");
        String subject = String.format(baseSubject.toString(),
                orderInfo.getAddress(), orderInfo.getPost(),
                orderInfo.getReceiver(), orderInfo.getMobile(),
                orderInfo.getUserMessage());
        paymentInfo.setSubject(subject);
        // 保存到数据库
        baseMapper.insert(paymentInfo);
    }

    /**
     * 更新订单状态
     * @param orderInfo
     * @param typeEnum
     * @param resultMap
     */
    @Override
    public void paySuccess(OrderInfo orderInfo, PaymentTypeEnum typeEnum, Map<String, String> resultMap) {
        // 根据商家订单id和订单支付方式查询出支付信息
        PaymentInfo paymentInfo = this.getPaymentInfo(orderInfo.getOutTradeNo(), typeEnum.getStatus());
        if (paymentInfo == null) { // 查询结果为空则参数错误
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 如果不是未支付状态就返回
        if (paymentInfo.getPaymentStatus() != PaymentStatusEnum.UNPAID.getStatus()) return;

        // 设置订单信息的微信支付订单
        paymentInfo.setTradeNo(resultMap.get("transaction_id")); // 微信订单号
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus()); // 支付成功
        paymentInfo.setCallbackTime(new Date()); // 回调日期
        paymentInfo.setCallbackContent(resultMap.toString()); // 回调内容
        this.updateById(paymentInfo);

        // 更新订单信息
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_SHIPMENTS.getStatus()); // 待发货
        orderInfo.setPayDate(new Date()); // 更新支付日期
        orderInfoService.updateById(orderInfo);
    }

    /**
     * 根据订单id删除支付记录
     * @param id
     */
    @Override
    public void removeByOrderId(String id) {
       QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
       wrapper.eq("order_id", id);
       baseMapper.delete(wrapper);
    }

    /**
     * 更新支付状态
     * @param orderId
     * @param status
     */
    @Override
    public void updatePaymentStatus(String orderId, PaymentStatusEnum status) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);
        paymentInfo.setPaymentStatus(status.getStatus());
        baseMapper.updateById(paymentInfo);
    }

    // 根据商家订单id和订单支付状态查询出支付信息
    private PaymentInfo getPaymentInfo(String outTradeNo, Integer status) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no", outTradeNo); // 商家订单号
        wrapper.eq("payment_type", status); // 支付方式
        return baseMapper.selectOne(wrapper);
    }
}
