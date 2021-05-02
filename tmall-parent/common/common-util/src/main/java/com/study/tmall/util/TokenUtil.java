package com.study.tmall.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 16:37
 * Versions:1.0.0
 * Description: token工具类，获取token中的password，userid等信息
 */
public class TokenUtil {

    private TokenUtil(){}

    /**
     * 获取token中的用户密码
     * @param request
     * @return
     */
    public static String getPassword(HttpServletRequest request){
        // 获取请求头中的token
        String token = request.getHeader("token");
        // 使用JwtHelper工具类获取token中的用户密码
        return JwtHelper.getPassword(token);
    }

    /**
     * 获取token中的用户ID
     * @param request
     * @return
     */
    public static String getUserId(HttpServletRequest request){
        // 获取请求头中的token
        String token = request.getHeader("token");
        // 使用JwtHelper工具类获取token中的用户ID
        return JwtHelper.getUserId(token);
    }
}
