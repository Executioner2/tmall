package com.study.tmall.enums;

import io.swagger.annotations.ApiModel;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 18:22
 * Versions:1.0.0
 * Description:
 */
@ApiModel(description = "图片类型枚举类")
public enum ImageTypeEnum {
    THUMBNAIL(0, "thumbnail"), // 缩略图  thumbnail
    DETAILS_FIGURE(1, "details"), // 详情图  details
    ;

    private Integer type;
    private String name;

    ImageTypeEnum(Integer type, String name){
        this.type = type;
        this.name = name;
    }

    public static String getNameByType(Integer type){
        ImageTypeEnum[] values = ImageTypeEnum.values();
        for (ImageTypeEnum obj : values) {
            if (type.intValue() == obj.getType().intValue()){
                return obj.getName();
            }
        }
        return "";
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
