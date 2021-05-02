package com.study.tmall.user.api;

import com.study.tmall.result.Result;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.vo.user.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 17:15
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "前台用户信息接口")
@RestController
@RequestMapping("/api/user/userInfo")
public class UserInfoApi {
    @Resource
    private UserInfoService userInfoService;

    // 发送邮箱验证码
    @ApiOperation("发送邮箱验证码")
    @PostMapping("/login/send/emailCode")
    private Result sendEmailCode(
            @ApiParam(value = "userLoginVo", name = "用户登录信息", required = true)
            @RequestBody UserLoginVo userLoginVo) {

        userInfoService.sendEmailCode(userLoginVo);
        return Result.ok();
    }

}
