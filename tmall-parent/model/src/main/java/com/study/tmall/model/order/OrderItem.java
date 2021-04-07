package com.study.tmall.model.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:09
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "订单项（购物车）表")
@TableName("order_item")
public class OrderItem extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id，逻辑外键")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty(value = "商品id，逻辑外键")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty(value = "用户id，逻辑外键")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "商品数量")
    @TableField("number")
    private Integer number;
}
