package com.study.tmall.vo.after_end;

import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-27 17:39
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "交易通知vo类")
public class DealNotifyVo {
    @ApiModelProperty("订单对象")
    private OrderInfo orderInfo;

    @ApiModelProperty("接收者邮箱")
    private String receiverEmail;

    @ApiModelProperty("订单项集合")
    private List<OrderItem> orderItemList;
}
