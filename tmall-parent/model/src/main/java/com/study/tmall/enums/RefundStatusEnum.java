package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 19:46
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "退款状态枚举类")
public enum RefundStatusEnum {
    UNREFUND(1,"退款中"),
    REFUND(2,"已退款"),
    ;

    private Integer status;
    private String name;

    RefundStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String getStatusNameByStatus(Integer status){
        RefundStatusEnum[] values = RefundStatusEnum.values();
        for (RefundStatusEnum obj : values) {
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
