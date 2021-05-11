package com.study.tmall.user.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-06 19:20
 * Versions:1.0.0
 * Description:
 */
@Component
public class ConstantWxPropertiesUtil implements InitializingBean {
    @Value("${wx.open.app_id}")
    private String appId;

    @Value("${wx.open.app_secret}")
    private String appSecret;

    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    @Value("${wx.open.binding_url}")
    private String bindingUrl;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;
    public static String WX_OPEN_BINDING_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
        WX_OPEN_BINDING_URL = bindingUrl;
    }
}
