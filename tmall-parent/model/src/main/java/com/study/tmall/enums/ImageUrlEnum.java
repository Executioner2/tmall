package com.study.tmall.enums;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 22:41
 * Versions:1.0.0
 * Description:
 */
public enum ImageUrlEnum {
    NGINX_IP("http://192.168.123.130"), // nginx的ip
    GROUP_1("group1"),
    ;

    private String str; // 图片服务器的ip

    ImageUrlEnum(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
