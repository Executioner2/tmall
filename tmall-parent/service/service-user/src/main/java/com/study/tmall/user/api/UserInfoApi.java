package com.study.tmall.user.api;

import com.study.tmall.result.Result;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.vo.user.UserLoginVo;
import com.study.tmall.vo.user.UserRegisterVo;
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
    public Result sendEmailCode(
            @ApiParam(value = "userLoginVo", name = "用户登录信息", required = true)
            @RequestBody UserLoginVo userLoginVo) {

        userInfoService.sendEmailCode(userLoginVo);
        return Result.ok();
    }

    // 用户登录
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result userLogin(
            @ApiParam(value = "userLoginVo", name = "用户登录信息", required = true)
            @RequestBody UserLoginVo userLoginVo) {

        String token = userInfoService.userLogin(userLoginVo);
        return Result.ok(token);
    }

    // 检测用户名是否被使用
    @ApiOperation("检测用户名是否被使用")
    @PostMapping("/register/nameRepeatCheck/{name}")
    public Result userNameRepeatCheck (
            @ApiParam(value = "name", name = "用户名", required = true)
            @PathVariable String name) {

        Boolean flag = userInfoService.userNameRepeatCheck(name);
        return Result.ok(flag);
    }

    // 检测邮箱是否被使用
    @ApiOperation("检测邮箱是否被使用")
    @PostMapping("/register/emailRepeatCheck/{email}")
    public Result userEmailRepeatCheck (
            @ApiParam(value = "email", name = "邮箱地址", required = true)
            @PathVariable String email) {

        Boolean flag = userInfoService.userEmailRepeatCheck(email);
        return Result.ok(flag);
    }

    // 注册发送验证码到邮箱
    @ApiOperation("注册发送验证码到邮箱")
    @PostMapping("/register/send/{email}")
    public Result sendEmailCode (
            @ApiParam(value = "email", name = "邮箱地址", required = true)
            @PathVariable String email) {

        userInfoService.sendEmailCode(email);
        return Result.ok();
    }

    // 用户注册
    @ApiOperation("用户注册")
    @PostMapping("/register/user")
    public Result userRegister(
            @ApiParam(value = "userRegisterVo", name = "用户注册对象", required = true)
            @RequestBody UserRegisterVo userRegisterVo) {

        userInfoService.userRegister(userRegisterVo);
        return Result.ok();
    }


}
