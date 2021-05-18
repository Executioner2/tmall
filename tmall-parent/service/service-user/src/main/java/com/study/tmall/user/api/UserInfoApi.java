package com.study.tmall.user.api;

import com.study.tmall.model.user.UserInfo;
import com.study.tmall.result.Result;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.vo.front.UserInfoVo;
import com.study.tmall.vo.user.UserLoginVo;
import com.study.tmall.vo.user.UserRegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
            @ApiParam(name = "userLoginVo", value = "用户登录信息", required = true)
            @RequestBody UserLoginVo userLoginVo) {

        userInfoService.sendEmailCode(userLoginVo);
        return Result.ok();
    }

    // 用户登录
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result userLogin(
            @ApiParam(name = "userLoginVo", value = "用户登录信息", required = true)
            @RequestBody UserLoginVo userLoginVo) {

        String token = userInfoService.userLogin(userLoginVo);
        return Result.ok(token);
    }

    // 检测用户名是否被使用
    @ApiOperation("检测用户名是否被使用")
    @PostMapping("/register/nameRepeatCheck/{name}")
    public Result userNameRepeatCheck (
            @ApiParam(name = "name", value = "用户名", required = true)
            @PathVariable String name) {

        Boolean flag = userInfoService.userNameRepeatCheck(name);
        return Result.ok(flag);
    }

    // 检测邮箱是否被使用
    @ApiOperation("检测邮箱是否被使用")
    @PostMapping("/register/emailRepeatCheck/{email}")
    public Result userEmailRepeatCheck (
            @ApiParam(name = "email", value = "邮箱地址", required = true)
            @PathVariable String email) {

        Boolean flag = userInfoService.userEmailRepeatCheck(email);
        return Result.ok(flag);
    }

    // 注册发送验证码到邮箱
    @ApiOperation("注册发送验证码到邮箱")
    @PostMapping("/register/send/{email}")
    public Result sendEmailCode (
            @ApiParam(name = "email", value = "邮箱地址", required = true)
            @PathVariable String email) {

        userInfoService.sendEmailCode(email);
        return Result.ok();
    }

    // 用户注册
    @ApiOperation("用户注册")
    @PostMapping("/register/user")
    public Result userRegister(
            @ApiParam(name = "userRegisterVo", value = "用户注册对象", required = true)
            @RequestBody UserRegisterVo userRegisterVo) {

        userInfoService.userRegister(userRegisterVo);
        return Result.ok();
    }

    // 用户邮箱绑定
    @ApiOperation("用户邮箱绑定")
    @PostMapping("/register/emailBinding/{token}")
    public Result emailBinding(
            @ApiParam(name = "token", value = "token", required = true)
            @PathVariable String token,

            @ApiParam(name = "userRegisterVo", value = "用户注册对象", required = true)
            @RequestBody UserRegisterVo userRegisterVo) {

        token = userInfoService.emailBinding(token, userRegisterVo);
        return Result.ok(token);
    }

    // 根据token获取用户信息
    @ApiOperation("获取用户信息")
    @PostMapping("/auth/getUserInfo")
    public Result getUserInfoVoByToken(
            @ApiParam(name = "request", value = "HttpServletRequest", required = true)
            HttpServletRequest request) {
        String token = request.getHeader("token");
        UserInfoVo userInfoVo = userInfoService.getUserInfoVoByToken(token);
        return Result.ok(userInfoVo);
    }

    // 根据token获取用户详情信息
    @ApiOperation("根据token获取用户详情信息")
    @PostMapping("/auth/getUserDetailsInfo")
    public Result getUserDetailsInfoByToken(
            @ApiParam(name = "request", value = "HttpServletRequest", required = true)
            HttpServletRequest request) {
        String token = request.getHeader("token");
        UserInfo userInfo = userInfoService.getUserDetailsInfoByToken(token);
        return Result.ok(userInfo);
    }

    // 解除微信绑定
    @ApiOperation("解除微信绑定")
    @PostMapping("/auth/unWechatBinding")
    public Result unWechatBinding(
            @ApiParam(name = "request", value = "HttpServletRequest", required = true)
            HttpServletRequest request) {
        String token = request.getHeader("token");
        userInfoService.unWechatBinding(token);
        return Result.ok();
    }

    // 修改用户昵称
    @ApiOperation("修改用户昵称")
    @PostMapping("/auth/update/nickName/{nickName}")
    public Result updateNickName(
            @ApiParam(name = "nickName", value = "用户昵称", required = true)
            @PathVariable String nickName,

            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {
        String token = request.getHeader("token");
        userInfoService.updateNickName(token, nickName);
        return Result.ok();
    }

    // 用户头像上传
    @ApiOperation("用户头像上传")
    @PostMapping("/auth/upload/avatar")
    public Result uploadAvatar(
            @ApiParam(name = "file", value = "头像文件", required = true)
            MultipartFile file,

            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {

        String token = request.getHeader("token");
        userInfoService.uploadAvatar(token, file);
        return Result.ok();
    }

    // 根据token查询用户
    @ApiOperation("根据token查询用户")
    @PostMapping("inner/getUserInfo/{token}")
    public UserInfo getUserInfoByToken(
            @ApiParam(name = "token", value = "token", required = true)
            @PathVariable("token") String token) {

        return userInfoService.getUserInfoByToken(token);
    }
}
