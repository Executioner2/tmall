package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-22 17:37
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "订单项商品是否评价枚举类")
public enum ReviewEnum {
    NOT_REVIEW(0), // 未评价
    REVIEW(1), // 已评价
    OVERTIME(-1), // 超时未评价
    ;

    private Integer status;

    ReviewEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
