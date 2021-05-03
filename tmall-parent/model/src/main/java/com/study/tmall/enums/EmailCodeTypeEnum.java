package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-03 16:50
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "邮箱验证码类型枚举")
public enum  EmailCodeTypeEnum {
    LOGIN_CODE(1, "login#"), // 登录验证码，#为了和邮箱地址进行分隔
    REGISTER_CODE(2, "register#"), // 注册验证码
    ;

    private Integer number;
    private String type;

    EmailCodeTypeEnum(Integer number, String type) {
        this.number = number;
        this.type = type;
    }

    // 根据number获取type
    public static String getTypeByNumber(Integer number) {
        EmailCodeTypeEnum[] values = EmailCodeTypeEnum.values();
        for (EmailCodeTypeEnum obj : values) {
            if (number.intValue() == obj.getNumber().intValue()) {
                return obj.getType();
            }
        }
        return "";
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
