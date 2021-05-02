package com.study.tmall.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-02 12:15
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "前台用户登录vo类")
public class UserLoginVo {
    @ApiModelProperty("用户登录账号（id  手机号  邮箱）")
    private String account;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("邮箱验证码（6位）")
    private String emailCode;
}
