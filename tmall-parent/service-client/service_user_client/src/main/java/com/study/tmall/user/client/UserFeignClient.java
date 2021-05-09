package com.study.tmall.user.client;

import com.study.tmall.model.user.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-21 18:40
 * Versions:1.0.0
 * Description:
 */
@Component
@FeignClient("service-user")
public interface UserFeignClient {
    // 获取用户基本信息，内部调用
    @ApiModelProperty(value = "获取用户基本信息，内部调用")
    @PostMapping("/admin/user/userInfo/inner/listUserInfo")
    List<UserInfo> listUserInfoOfInner(@RequestBody List<String> idList);
}
