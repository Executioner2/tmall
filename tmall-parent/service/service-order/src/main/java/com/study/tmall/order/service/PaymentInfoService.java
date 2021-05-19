package com.study.tmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.enums.PaymentTypeEnum;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.PaymentInfo;

import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:07
 * Versions:1.0.0
 * Description:
 */
public interface PaymentInfoService extends IService<PaymentInfo> {
    // 保存订单信息到支付信息
    void savePaymentInfo(OrderInfo orderInfo, PaymentTypeEnum typeEnum);

    // 更新订单状态
    void paySuccess(OrderInfo orderInfo, PaymentTypeEnum typeEnum, Map<String, String> resultMap);
}
