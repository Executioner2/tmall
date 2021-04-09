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
    ;

    private String ip; // 图片服务器的ip

    ImageUrlEnum(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
