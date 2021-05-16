package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-16 15:16
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "算术运算类型枚举类")
public enum ArithmeticTypeEnum {
    ADD(0), // 加法
    SUBTRACT(1), // 减法
    MULTIPLY(2), // 乘法
    DIVIDE(3), // 除法
    REMAINDER(4), // 取余
    ;

    private Integer type;

    ArithmeticTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
