package com.study.tmall.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 21:46
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "前台系统商品查询vo类")
public class ProductInfoFrontQueryVo {
    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "商品最低价格")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "商品最高价格")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "排序字段") // 0:默认 1:评价数 2:新品 3:月成交量 4:价格
    private Integer sortField;

    @ApiModelProperty(value = "上一次排序方式")
    private Integer lostSortField;

    @ApiModelProperty(value = "排序方式")
    private Integer sortType;
}
