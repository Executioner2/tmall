package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 19:11
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "支付状态枚举类")
public enum PaymentStatusEnum {
    FAIL(0, "支付失败"),
    UNPAID(1,"支付中"),
    PAID(2, "已支付")
    ;

    private Integer status;
    private String name;

    PaymentStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getStatusNameByStatus(Integer status){
        PaymentStatusEnum[] values = PaymentStatusEnum.values();
        for (PaymentStatusEnum obj : values) {
            if (status.intValue() == obj.getStatus().intValue()){
                return obj.getName();
            }
        }
        return "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
