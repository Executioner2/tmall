package com.study.tmall.model.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 17:30
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "分类属性名")
@TableName("property")
public class Property extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id，逻辑外键")
    @TableField("category_id")
    private String categoryId;

    @ApiModelProperty(value = "分类属性名称")
    @TableField("name")
    private String name;
}
