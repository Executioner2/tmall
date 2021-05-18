package com.study.tmall.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-18 18:58
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "下单vo类")
public class SettlementVo {
    @ApiModelProperty("收货地址")
    private String address;

    @ApiModelProperty("邮政编码")
    private String post;

    @ApiModelProperty("收货人姓名")
    private String receiver;

    @ApiModelProperty("用户留言")
    private String userMessage;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("订单项id集合")
    private List<String> orderItemIdList;

}
