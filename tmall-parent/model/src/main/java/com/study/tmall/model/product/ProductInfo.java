package com.study.tmall.model.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 17:43
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品信息表")
@TableName("product_info")
public class ProductInfo extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id，逻辑外键")
    @TableField("category_id")
    private String categoryId;

    @ApiModelProperty(value = "商品名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "商品小标题")
    @TableField("sub_title")
    private String subTitle;

    @ApiModelProperty(value = "商品原价")
    @TableField("orignal_price")
    private BigDecimal orignalPrice;

    @ApiModelProperty(value = "商品实际售价")
    @TableField("promote_price")
    private BigDecimal promotePrice;

    @ApiModelProperty(value = "库存")
    @TableField("stock")
    private Long stock;

    @ApiModelProperty(value = "总销售量")
    @TableField("sales_volume")
    private Long salesVolume;

    @ApiModelProperty(value = "月销售量")
    @TableField("monthly_sales")
    private Long monthlySales;
}
