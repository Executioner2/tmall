package com.study.tmall.model.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:00
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "订单信息表")
@TableName("order_info")
public class OrderInfo extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id，逻辑外键")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "商家订单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "收货地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "邮政编码")
    @TableField("post")
    private String post;

    @ApiModelProperty(value = "收件人")
    @TableField("receiver")
    private String receiver;

    @ApiModelProperty(value = "收件人手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "用户留言")
    @TableField("user_message")
    private String userMessage;

    @ApiModelProperty(value = "订单状态(0:待付款  1:待发货  2:待收货  3:待评价)")
    @TableField("order_status")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单生成日期") // 记录生成日期在数据库中用的timestamp，订单创建日期用的datetime
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value = "买家支付日期")
    @TableField("pay_date")
    private Date payDate;

    @ApiModelProperty(value = "卖家发货日期")
    @TableField("delivery_date")
    private Date deliveryDate;

    @ApiModelProperty(value = "买家确认收货日期")
    @TableField("confirm_date")
    private Date confirmDate;
}
