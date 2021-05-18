package com.study.tmall.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.result.Result;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.vo.user.UserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 15:28
 * Versions:1.0.0
 * Description:
 */
@RestController
@Api(tags = "用户管理")
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    // 分页条件显示用户
    @ApiOperation(value = "分页条件显示用户")
    @GetMapping("/list/{current}/{limit}")
    public Result findPageUserInfo(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "userQueryVo", value = "查询条件vo", required = true)
            UserQueryVo userQueryVo){

        Page<UserInfo> page = new Page<>(current, limit);
        IPage<UserInfo> pageModule = userInfoService.findPageUserInfo(page, userQueryVo);
        return Result.ok(pageModule);
    }

    // 锁定\解锁 用户
    @ApiOperation(value = "锁定 or 解锁 用户")
    @PutMapping("/lock/{id}/{status}")
    public Result lock(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable String id,

            @ApiParam(name = "status", value = "锁定状态", required = true)
            @PathVariable Integer status){

        userInfoService.lock(id, status);
        return Result.ok();
    }

    // 审批用户认证
    @ApiOperation(value = "审批用户认证")
    @PutMapping("/authUser/{id}/{authStatus}")
    public Result authUser(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable String id,

            @ApiParam(name = "authStatus", value = "认证状态", required = true)
            @PathVariable Integer authStatus){

        userInfoService.authUser(id, authStatus);
        return Result.ok();
    }

    // 获取用户基本信息，内部调用
    @ApiOperation(value = "获取用户基本信息，内部调用")
    @PostMapping("/inner/listUserInfo")
    public List<UserInfo> listUserInfoOfInner(
            @ApiParam(name = "idList", value = "用户id集合", required = true)
            @RequestBody List<String> idList){

        return userInfoService.listUserInfo(idList);
    }

}
