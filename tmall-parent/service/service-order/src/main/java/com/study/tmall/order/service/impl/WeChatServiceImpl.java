package com.study.tmall.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.WeChatService;
import com.study.tmall.order.util.ConstantPropertiesUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-18 19:58
 * Versions:1.0.0
 * Description:
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private OrderInfoService orderInfoService;

    /** TODO 以后改成自己的商户，且使用最新的微信支付V3版本，此方法代码会大改，会重新添加依赖
     * 生成微信支付二维码
     * @param orderId
     * @return
     */
    @Override
    public Map createNative(String orderId) {
        // 先通过订单id查询redis，如果有
        Map payMap = (Map) redisTemplate.opsForValue().get(orderId);
        if (payMap != null) {
            return payMap;
        }

        // 根据订单号获取订单信息
        OrderInfo orderInfo = orderInfoService.getById(orderId);

        // 封装请求参数
        Map paramMap = new HashMap();
        paramMap.put("appid", ConstantPropertiesUtil.APPID); // 关联的公众号id
        paramMap.put("mch_id", ConstantPropertiesUtil.PARTNER); // 商户号
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串
        paramMap.put("body", "用户留言：" + orderInfo.getUserMessage()); // 商品描述
        paramMap.put("out_trade_no", orderInfo.getOutTradeNo()); // 商家订单号
        //paramMap.put("total_fee", )
        paramMap.put("total_fee", "1"); // 金额，单位分

        return null;
    }
}
