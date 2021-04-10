package com.study.tmall.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 18:13
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品图片返回到前端的vo")
public class ProductImageReturnVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "图片访问地址")
    private String url;
}
