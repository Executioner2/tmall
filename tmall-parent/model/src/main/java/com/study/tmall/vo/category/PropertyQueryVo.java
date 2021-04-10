package com.study.tmall.vo.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-08 11:47
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "属性查询vo类")
public class PropertyQueryVo {
    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "创建日期开始")
    private Date createTimeBegin;

    @ApiModelProperty(value = "创建日期结束")
    private Date createTimeEnd;
}
