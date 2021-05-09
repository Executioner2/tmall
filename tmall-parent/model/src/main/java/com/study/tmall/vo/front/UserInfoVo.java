package com.study.tmall.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-08 20:15
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "前端页头用户基本信息")
public class UserInfoVo {
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("购物车商品数量")
    private Integer productNumber;
}
