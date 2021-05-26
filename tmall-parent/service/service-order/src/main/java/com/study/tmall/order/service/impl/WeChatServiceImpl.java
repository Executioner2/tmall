package com.study.tmall.order.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.wxpay.sdk.WXPayUtil;
import com.study.tmall.enums.PaymentStatusEnum;
import com.study.tmall.enums.PaymentTypeEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.PaymentInfoService;
import com.study.tmall.order.service.WeChatService;
import com.study.tmall.order.util.ConstantPropertiesUtil;
import com.study.tmall.order.util.HttpClient;
import com.study.tmall.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private PaymentInfoService paymentInfoService;

    /** TODO 以后改成自己的商户，且使用最新的微信支付V3版本，此方法代码会大改，会重新添加依赖
     * 生成微信支付二维码
     * @param orderId
     * @return
     */
    @Override
    public Map createNative(String orderId) {
        try {
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
            paramMap.put("body", "tmall-v1.0在线支付"); // 商品描述
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo()); // 商家订单号
            // 金额，因为微信支付的单位是分，所以扩大100倍
            paramMap.put("total_fee", orderInfo.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee", "1"); // 金额，单位分
            paramMap.put("spbill_create_ip", "127.0.0.1"); // 终端ip
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify"); // 接收微信支付结果通知的url
            paramMap.put("trade_type", "NATIVE"); // 交易类型

            // HttpClient根据url访问第三方接口并传递参数
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置参数，并把商户的sign写进去
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtil.PARTNERKEY));
            httpClient.setHttps(true); // 支持https
            httpClient.post(); // 发送post请求，执行这个方法就发送过去了
            // 获取返回信息，是xml格式的字符串
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            // 封装返回结果集
            System.out.println(resultMap);
            Map map = new HashMap();
            map.put("orderId", orderId);
            map.put("amount", orderInfo.getAmount());
            map.put("resultCode", resultMap.get("return_code"));
            map.put("codeUrl", resultMap.get("code_url"));

            // 保存订单信息到支付信息
            paymentInfoService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN);
            if(!StringUtils.isEmpty(resultMap.get("result_code"))) {
                // TODO 开启定时任务，2小时未支付取消订单
                // 微信支付二维码2小时过期，可采取2小时未支付取消订单
                redisTemplate.opsForValue().set(orderId, map, 1000, TimeUnit.MINUTES);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 支付结果轮询
     * @param orderId
     * @return
     */
    @Override
    public String payStatusPoll(String orderId) {
        try {
            OrderInfo orderInfo = orderInfoService.getById(orderId);
            if (orderInfo == null) {
                throw new TmallException(ResultCodeEnum.PARAM_ERROR);
            }

            // 封装参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtil.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtil.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            // 发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true); // 支持https
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtil.PARTNERKEY)); // 设置参数
            httpClient.post();
            // 返回结果
            String content = httpClient.getContent();
            Map resultMap = WXPayUtil.xmlToMap(content);

            // 根据返回结果处理请求
            String returnCode = (String) resultMap.get("return_code"); // 这个是返回状态不是交易状态
            String tradeState = (String) resultMap.get("trade_state"); // 交易是否成功看这个
            if (!StringUtils.isEmpty(returnCode) && "SUCCESS".equals(tradeState)) {
                // 更新订单状态
                paymentInfoService.paySuccess(orderInfo, PaymentTypeEnum.WEIXIN, resultMap);
                return PaymentStatusEnum.PAID.getName(); // 返回支付成功
            }
            return PaymentStatusEnum.UNPAID.getName(); // 返回支付中
        } catch (Exception e) {
            e.printStackTrace();
            // 返回支付失败
            return PaymentStatusEnum.FAIL.getName();
        }
    }

    /**
     * TODO 点击支付按钮直接完成支付功能，这是为了方便测试
     * @param orderId
     * @return
     */
    @Override
    public String pay(String orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if (orderInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 假数据
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("transaction_id", RandomUtil.randomString(16));
        resultMap.put("context", "这是一个为了方便支付测试的方法");
        // 更新订单状态
        paymentInfoService.paySuccess(orderInfo, PaymentTypeEnum.WEIXIN, resultMap);
        return PaymentStatusEnum.PAID.getName(); // 返回支付成功
    }
}
