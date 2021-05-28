package com.study.tmall.email.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.study.tmall.email.service.EmailService;
import com.study.tmall.email.util.ConstantPropertiesUtil;
import com.study.tmall.enums.EmailCodeTypeEnum;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.dto.DealNotify;
import com.study.tmall.vo.front.EmailCodeVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 21:36
 * Versions:1.0.0
 * Description:
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用来发送模版邮件
     */
    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送内容到目标邮箱
     * @param to
     * @param subject
     * @param text
     */
    @Override
    public void send(String to, String subject, String text) {
        // 设置template中的参数
        Context context = new Context();
        context.setVariable("project", ConstantPropertiesUtil.PROJECT);
        context.setVariable("author", ConstantPropertiesUtil.AUTHOR);
        context.setVariable("code", text);
        // 把template（code.html）当作内容发送
        String emailContent = templateEngine.process("code", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(ConstantPropertiesUtil.FROM);  // 发送方
            helper.setTo(to); // 接收方
            helper.setSubject(subject); // 发送主题
            helper.setText(emailContent, true); // 发送的内容（一个网页）
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    /**
     * 发送验证码到目标邮箱
     * @param emailCodeVo
     */
    @Override
    public void sendCode(EmailCodeVo emailCodeVo) {
        // 获取邮箱
        String to = emailCodeVo.getEmail();
        // 获取验证码类型
        String codeType = EmailCodeTypeEnum.getTypeByNumber(emailCodeVo.getType());
        // 先查询redis中是否已经有验证码了，有就返回
        Object o = stringRedisTemplate.opsForValue().get(codeType + to);
        if (o != null) {
            return;
        }
        // 生成6位随机验证码
        String code = String.valueOf(RandomUtil.randomInt(100000, 999999));

        // 设置template中的参数
        Context context = new Context();
        context.setVariable("project", ConstantPropertiesUtil.PROJECT);
        context.setVariable("author", ConstantPropertiesUtil.AUTHOR);
        context.setVariable("code", code);
        // 把template当作内容发送
        String emailContent = templateEngine.process("mail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(ConstantPropertiesUtil.FROM);  // 发送方
            helper.setTo(to); // 接收方
            helper.setSubject("tmall login code"); // 发送主题
            helper.setText(emailContent, true); // 发送的内容（一个网页）
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("开始发送");
        mailSender.send(message);
        System.out.println("已经发送");

        // 写入到redis中去
        stringRedisTemplate.opsForValue().set(codeType + to, code, 10, TimeUnit.MINUTES); // 验证码有效时间 十分钟
    }

    /**
     * 删除redis中的邮箱验证码
     * @param key
     */
    @Override
    public void delCode(String key) {
       stringRedisTemplate.delete(key);
    }

    /**
     * 订单状态通知
     * @param dealNotify
     */
    @Override
    public void dealNotify(DealNotify dealNotify) {
        // 获取接收者邮箱， 订单信息 和 订单项集合
        String to = dealNotify.getReceiverEmail();
        OrderInfo orderInfo = dealNotify.getOrderInfo();
        List<OrderItem> orderItemList = dealNotify.getOrderItemList();

        String hint = "";
        String status = "";

        // 根据订单状态编号设置提示和订单状态中文字符
        Integer orderStatus = orderInfo.getOrderStatus();
        if (orderStatus.intValue() == OrderStatusEnum.WAIT_TAKE_GOODS.getStatus()) {
            // 是待收货，那么卖家已发货
            hint = "您在tmall-v1.0购买的商品已发货，请耐心等待快递运输！";
            status = "已发货";
        } else if (orderStatus.intValue() == OrderStatusEnum.WAIT_REVIEW.getStatus()) {
            // 是待评价，那么买家已签收
            hint = "您在tmall-v1.0购买的商品已被签收，请给您购买的商品一个评价吧！";
            status = "已签收";
        }

        // 创建日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createDate = sdf.format(orderInfo.getCreateDate());

        // 设置template中的参数
        Context context = new Context();
        context.setVariable("project", ConstantPropertiesUtil.PROJECT);
        context.setVariable("author", ConstantPropertiesUtil.AUTHOR);
        context.setVariable("hint", hint);
        context.setVariable("status", status);
        context.setVariable("createDate", createDate);
        context.setVariable("orderInfo", orderInfo);
        context.setVariable("orderItemList",orderItemList);
        // 把template当作内容发送
        String emailContent = templateEngine.process("dealNotify", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(ConstantPropertiesUtil.FROM);  // 发送方
            helper.setTo(to); // 接收方
            helper.setSubject("tmall deal info"); // 发送主题
            helper.setText(emailContent, true); // 发送的内容（一个网页）
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("通知开始发送");
        mailSender.send(message);
        System.out.println("通知已发送");
    }
}
