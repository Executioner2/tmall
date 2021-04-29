package com.study.tmall.user.api;

import com.study.tmall.user.service.UserInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
