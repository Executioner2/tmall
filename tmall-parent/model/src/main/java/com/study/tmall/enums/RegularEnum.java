package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-02 17:09
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "常用正则表达式实例枚举")
public enum RegularEnum {
    USER_NAME_LOGIN("^[a-zA-Z0-9_]{3,15}$"), // 用户名登录
    USER_EMAIL_LOGIN("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"), // 用户邮箱登录
    USER_PHONE_LOGIN("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$"), // 用户手机号登陆
    ;

    private String regex;

    RegularEnum(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
