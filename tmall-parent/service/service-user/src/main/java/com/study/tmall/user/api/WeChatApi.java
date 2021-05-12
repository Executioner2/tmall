package com.study.tmall.user.api;

import com.study.tmall.result.Result;
import com.study.tmall.user.service.WeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-06 19:22
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "微信接口")
@RestController
@RequestMapping("/api/user/weChat")
public class WeChatApi {
    @Resource
    private WeChatService weChatService;

    // 获取登录二维码
    @ApiOperation("获取登录二维码")
    @GetMapping("/get/QRCode/{type}")
    public Result weChatQRCode(
            @ApiParam(name = "type", value = "回调接口", required = true)
            @PathVariable Integer type) {
        Map<String, String> map = weChatService.weChatQRCode(type);
        return map == null ? null : Result.ok(map);
    }

    // 用户扫码成功后的回调接口
    @ApiOperation("用户扫码成功后的回调接口")
    @RequestMapping("/callback")
    public Result callback(
            @ApiParam(name = "code", value = "从微信获取accessToken的票据", required = true)
            String code,

            @ApiParam(name = "state", value = "把获取到的accessToken存入redis中的key", required = true)
            String state) {

        Boolean flag = weChatService.callback(code, state);
        return flag ? Result.ok("登录成功！") : Result.fail("登陆失败！");
    }

    // 用户登录扫码状态轮询
    @ApiOperation("用户登录扫码状态轮询")
    @PostMapping("/polling/{state}")
    public Result polling(
            @ApiParam(name = "state", value = "登录信息查询凭证", required = true)
            @PathVariable String state) {

        Map<String, Object> map = weChatService.polling(state);
        return Result.ok(map);
    }

    // 微信绑定回调函数
    @ApiOperation("微信绑定回调函数")
    @RequestMapping("/binding/callback")
    public Result bindingCallback(
            @ApiParam(name = "code", value = "从微信获取accessToken的票据", required = true)
            String code,

            @ApiParam(name = "state", value = "把获取到的accessToken存入redis中的key", required = true)
            String state
    ) {

        Boolean flag = weChatService.bindingCallback(code, state);
        return flag ? Result.ok("扫码成功，请在网页上确认绑定！") : Result.ok("扫码失败！");
    }

    // 微信绑定轮询
    @ApiOperation("微信绑定轮询")
    @PostMapping("/auth/polling/binding/{state}")
    public Result pollingBinding(
            @ApiParam(name = "state", value = "扫码查询凭证", required = true)
            @PathVariable String state) {

        Boolean flag = weChatService.pollingBinding(state);
        return Result.ok(flag);
    }

    // 确认进行微信绑定
    @ApiOperation("确认进行微信绑定")
    @PostMapping("/auth/confirm/weChat/binding/{state}")
    public Result confirmWeChatBinding(
            @ApiParam(name = "state", value = "微信绑定凭证", required = true)
            @PathVariable String state,

            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request) {

        // 获得用户id
        String token = request.getHeader("token");
        Boolean flag = weChatService.confirmWeChatBinding(token, state);
        return Result.ok(flag);
    }

}
