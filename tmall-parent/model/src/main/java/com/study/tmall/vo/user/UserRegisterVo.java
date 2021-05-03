package com.study.tmall.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-03 14:21
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "用户注册vo类")
public class UserRegisterVo {
    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("验证码")
    private String emailCode;
}
