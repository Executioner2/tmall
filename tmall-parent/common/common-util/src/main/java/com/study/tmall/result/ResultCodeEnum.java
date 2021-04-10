package com.study.tmall.result;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 20:00
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "返回状态码枚举类")
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    PARAM_ERROR( 202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    DATA_UPDATE_ERROR(205, "数据版本异常"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    CODE_ERROR(210, "验证码错误"),
    LOGIN_DISABLED_ERROR(212, "该用户已被禁用"),
    REGISTER_MOBLE_ERROR(213, "手机号已被使用"),
    REGISTER_EMAIL_ERROR(214, "邮箱已被使用"),
    LOGIN_AURH(215, "需要登录"),
    LOGIN_ACL(216, "没有权限"),

    URL_ENCODE_ERROR( 217, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 218, "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD( 219, "获取accessToken失败"),
    FETCH_USERINFO_ERROR( 220, "获取用户信息失败"),

    PAY_RUN(221, "支付中"),
    CANCEL_ORDER_FAIL(225, "取消订单失败"),
    CANCEL_ORDER_NO(225, "不能取消预约"),

    DELETE_FAIL(301, "删除失败")
    ;

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
