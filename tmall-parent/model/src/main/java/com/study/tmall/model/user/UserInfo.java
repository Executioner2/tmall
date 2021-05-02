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
 * Date:2021-04-06 17:33
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "用户信息表")
@TableName("user_info")
public class UserInfo extends BaseEntity {
    @ApiModelProperty(value = "序列化版本")
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "微信号")
    @TableField("openid")
    private String openid;

    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "用户手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "用户邮箱地址")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "证件类型：身份证，永久居留证等")
    @TableField("certificates_type")
    private String certificatesType;

    @ApiModelProperty(value = "证件编号")
    @TableField("certificates_no")
    private String certificatesNo;

    @ApiModelProperty(value = "证件图片url地址")
    @TableField("certificates_url")
    private String certificatesUrl;

    @ApiModelProperty(value = "认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）")
    @TableField("auth_status")
    private Integer authStatus;

    @ApiModelProperty(value = "锁定状态（0：锁定 1：正常）")
    @TableField("status")
    private Integer status;
}
