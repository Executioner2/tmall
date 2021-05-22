package com.study.tmall.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-22 21:10
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "评价vo类")
public class ReviewVo {
    @ApiModelProperty("订单项id")
    private String orderItemId;

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("评价内容")
    private String content;
}
