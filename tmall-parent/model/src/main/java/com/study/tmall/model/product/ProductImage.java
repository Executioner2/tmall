package com.study.tmall.model.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 17:49
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品图片表")
@TableName("product_image")
public class ProductImage extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品id，逻辑外键")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty(value = "图片类型(0: 缩略图  1:详情图)")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "图片的url地址")
    @TableField("url")
    private String url;
}
