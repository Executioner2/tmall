package com.study.tmall.user.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 15:28
 * Versions:1.0.0
 * Description:
 */
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户管理")
public class UserController {
    @GetMapping("/test/{value}")
    public String test(@PathVariable String value){
        return value;
    }
}
