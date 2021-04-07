package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:17
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "认证状态枚举类")
public enum  AuthStatusEnum {

    NO_AUTH(0, "未认证"),
    AUTH_RUN(1, "认证中"),
    AUTH_SUCCESS(2, "认证成功"),
    AUTH_FAIL(-1, "认证失败"),
    ;

    private Integer status;
    private String name;

    AuthStatusEnum(Integer status, String  name){
        this.status = status;
        this.name = name;
    }


    /**
     * 根据认证status获得认证name
     * @param status
     * @return
     */
    public static String getStatusNameByStatus(Integer status){
        AuthStatusEnum[] values = AuthStatusEnum.values();
        for (AuthStatusEnum obj : values) {
            if(status.intValue() == obj.getStatus().intValue()){
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
