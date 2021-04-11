package com.study.tmall.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-08 12:02
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "用户查询vo类")
public class UserQueryVo {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户手机号 昵称 姓名")
    private String keyword;

    @ApiModelProperty(value = "用户认证状态")
    private Integer authStatus;

    @ApiModelProperty(value = "用户锁定状态")
    private Integer status;
}
