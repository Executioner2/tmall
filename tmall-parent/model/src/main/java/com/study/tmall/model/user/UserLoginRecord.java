package com.study.tmall.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.study.tmall.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 17:39
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "用户登录限制表")
@TableName("user_login_record")
public class UserLoginRecord extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id，逻辑外键")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户ip记录")
    @TableField("ip")
    private String ip;
}
