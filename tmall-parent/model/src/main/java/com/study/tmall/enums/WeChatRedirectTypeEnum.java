package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-11 19:22
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "微信回调类型枚举类")
public enum WeChatRedirectTypeEnum {
    LOGIN(0), // 微信登录
    BINDING(1), // 微信绑定
    ;

    private Integer type;

    WeChatRedirectTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
