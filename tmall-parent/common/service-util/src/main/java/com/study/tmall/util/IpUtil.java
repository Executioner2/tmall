package com.study.tmall.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 17:59
 * Versions:1.0.0
 * Description:
 */
public class IpUtil {
    /**
     * 获取请求用户的真实ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
