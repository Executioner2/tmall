package com.study.tmall.dto;

import com.study.tmall.enums.ArithmeticTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-16 17:26
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品库存传输类")
public class ProductStock {
    @ApiModelProperty("订单项id")
    private String id;

    @ApiModelProperty("商品id")
    private String productId;

    @ApiModelProperty("更新数量")
    private Integer number;

    @ApiModelProperty("更新规则（加 还是 减）")
    private ArithmeticTypeEnum type;
}
