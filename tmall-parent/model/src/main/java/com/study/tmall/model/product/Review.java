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
 * Date:2021-04-06 17:56
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品评价表")
@TableName("review")
public class Review extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id，逻辑外键")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "商品id，逻辑外键")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty(value = "评价内容")
    @TableField("content")
    private String content;
}
