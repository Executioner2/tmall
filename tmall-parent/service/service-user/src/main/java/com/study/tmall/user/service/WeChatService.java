package com.study.tmall.user.service;

import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-06 20:25
 * Versions:1.0.0
 * Description:
 */
public interface WeChatService {
    // 获取微信登录二维码
    Map<String, String> weChatQRCode(Integer type);

    // 用户扫码成功后的回调接口
    Boolean callback(String code, String state);

    // 用户登录扫码状态轮询
    Map<String, Object> polling(String state);

    // 微信绑定回调函数
    Boolean bindingCallback(String code, String state);

    // 微信绑定轮询
    Boolean pollingBinding(String state);

    // 确认进行微信绑定
    Boolean confirmWeChatBinding(String token, String state);
}
