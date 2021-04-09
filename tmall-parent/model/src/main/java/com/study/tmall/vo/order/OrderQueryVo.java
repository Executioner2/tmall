package com.study.tmall.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 14:56
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "订单查询vo类")
public class OrderQueryVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "创建日期开始")
    private Date createTimeBegin;

    @ApiModelProperty(value = "创建日期结束")
    private Date createTimeEnd;
}
