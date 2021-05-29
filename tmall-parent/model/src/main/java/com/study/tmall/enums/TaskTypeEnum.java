package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 14:48
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "任务类型")
public enum TaskTypeEnum {
    PAY_OVERTIME(1), // 商品支付超时
    REVIEW_OVERTIME(2), // 评价超时
    ;

    private Integer type;

    TaskTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
