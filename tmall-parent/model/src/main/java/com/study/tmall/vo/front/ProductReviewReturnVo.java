package com.study.tmall.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-28 20:40
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "商品评价返回vo类")
public class ProductReviewReturnVo {
    @ApiModelProperty(value = "评价id")
    private String id;

    @ApiModelProperty(value = "评价内容")
    private String content;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "匿名用户名")
    private String anonymity;

    @ApiModelProperty(value = "用户名")
    private String username;
}
