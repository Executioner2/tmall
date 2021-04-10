package com.study.tmall.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-08 11:48
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品查询vo类")
public class ProductQueryVo {
    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "商品名称 or 商品小标题")
    private String key;

    @ApiModelProperty(value = "商品最低价格")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "商品最高价格")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "创建日期开始")
    private Date createTimeBegin;

    @ApiModelProperty(value = "创建日期结束")
    private Date createTimeEnd;
}
