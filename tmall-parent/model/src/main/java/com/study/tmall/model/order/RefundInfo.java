package com.study.tmall.model.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:12
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "退款记录表")
@TableName("refund_info")
public class RefundInfo extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对外业务编号")
    @TableField("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "订单编号")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty(value = "支付类型（微信 支付宝）")
    @TableField("payment_type")
    private Integer paymentType;

    @ApiModelProperty(value = "交易编号")
    @TableField("trade_no")
    private String tradeNo;

    @ApiModelProperty(value = "退款金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "交易内容")
    @TableField("subject")
    private String subject;

    @ApiModelProperty(value = "退款状态")
    @TableField("refund_status")
    private Integer refundStatus;

    @ApiModelProperty(value = "回调时间")
    @TableField("callback_time")
    private Date callbackTime;

    @ApiModelProperty(value = "回调信息")
    @TableField("callback_content")
    private String callbackContent;
}
