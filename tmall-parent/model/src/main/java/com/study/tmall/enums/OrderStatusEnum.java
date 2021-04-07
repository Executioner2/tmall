package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:32
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "订单状态枚举类")
public enum OrderStatusEnum {
    WAIT_PAY(0, "待付款"),
    WAIT_SHIPMENTS(1, "待发货"),
    WAIT_TAKE_GOODS(2, "待收货"),
    WAIT_REVIEW(3, "待评价")
    ;

    private Integer status;
    private String name;

    OrderStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * 返回所有枚举值
     * @return 返回list集合，方便前端显示
     */
    public static List<Map<String, Object>> getStatusList(){
        List<Map<String, Object>> list = new ArrayList<>();
        OrderStatusEnum[] values = OrderStatusEnum.values();
        for (OrderStatusEnum obj : values) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", obj.getStatus());
            map.put("name", obj.getName());
            list.add(map);
        }
        return list;
    }

    public static String getStatusNameByStatus(Integer status){
        OrderStatusEnum[] values = OrderStatusEnum.values();
        for (OrderStatusEnum obj : values) {
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
