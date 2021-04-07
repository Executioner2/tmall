package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 19:26
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "支付类型枚举类")
public enum PaymentTypeEnum {
    ALIPAY(1, "支付宝"),
    WEIXIN(2, "微信");
    ;

    private Integer status;
    private String comment;

    PaymentTypeEnum(Integer status, String comment) {
        this.status = status;
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
