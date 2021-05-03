package com.study.tmall.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-03 16:48
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "邮箱验证码vo类")
public class EmailCodeVo {
    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("验证码类型")
    private Integer type;
}
