package com.study.tmall.vo.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-08 11:43
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "分类查询vo类")
public class CategoryQueryVo {

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "创建日期开始")
    private Date createTimeBegin;

    @ApiModelProperty(value = "创建日期结束")
    private Date createTimeEnd;
}
