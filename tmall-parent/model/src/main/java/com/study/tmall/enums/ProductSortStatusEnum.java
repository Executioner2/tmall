package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 22:14
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "前台系统商品排序状态枚举类")
public enum ProductSortStatusEnum {
    DEFAULT(0, ""), // 默认排序
    POPULAR(1, "reviews"), // 按人气（评价数）降序排序
    NEW_PRODUCTS(2, "create_time"), // 新品，按创建时间(从新到旧)降序排序
    SALES_VOLUME(3, "monthly_sales"), // 销量，按月销量降序排序
    PRICE(4, "promote_price"), // 按价格排序，
    ;

    private Integer number; // 排序编号
    private String field; // 字段名

    ProductSortStatusEnum(Integer number, String field) {
        this.number = number;
        this.field = field;
    }

    // 根据排序编号取得排序字段
    public static String getFieldByNumber(Integer number) {
        if (number == null) {
            return "";
        }
        ProductSortStatusEnum[] values = ProductSortStatusEnum.values();
        for (ProductSortStatusEnum obj : values) {
            if (number.intValue() == obj.getNumber().intValue()) {
                return obj.field;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

