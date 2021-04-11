package com.study.tmall.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 14:47
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "属性名和属性值的返回vo类")
public class PropertyAndValueVo {
    @ApiModelProperty(value = "属性值id")
    private String propertyValueId;

    @ApiModelProperty(value = "属性值")
    private String propertyValue;

    @ApiModelProperty(value = "属性名")
    private String propertyName;
}
