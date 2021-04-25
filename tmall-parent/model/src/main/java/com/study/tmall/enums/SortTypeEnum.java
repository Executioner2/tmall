package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 22:24
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "排序状态枚举类")
public enum SortTypeEnum {
    ASC(0, "asc"), // 升序
    DESC(1, "desc"), // 降序
    ;

    private Integer sort;
    private String type;

    SortTypeEnum(Integer sort, String type) {
        this.sort = sort;
        this.type = type;
    }

    // 根据排序代号取得排序类型
    public static String getTypeBySort(Integer sort) {
        if (sort == null) {
            return "";
        }
        SortTypeEnum[] values = SortTypeEnum.values();
        for (SortTypeEnum obj : values) {
            if (sort.intValue() == obj.getSort().intValue()) {
                return obj.type;
            }
        }
        return "";
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
