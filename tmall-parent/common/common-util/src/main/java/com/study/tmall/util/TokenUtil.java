package com.study.tmall.util;

import com.study.tmall.exception.TmallException;
import com.study.tmall.result.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;

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
     * 根据参数生成token
     * @param userId
     * @param password
     * @return
     */
    public static String createToken(String userId, String password) {
        return JwtHelper.createToken(userId, password);
    }

    /**
     * 获取token中的用户密码
     * @param request
     * @return
     */
    public static String getPassword(HttpServletRequest request){
        try {
            // 获取请求头中的token
            String token = request.getHeader("token");
            // 使用JwtHelper工具类获取token中的用户密码
            String password = JwtHelper.getPassword(token);
            return password;
        } catch (ExpiredJwtException e) {
            throw new TmallException(ResultCodeEnum.LOGIN_AUTH);
        }
    }

    /**
     * 获取token中的用户密码
     * @param token
     * @return
     */
    public static String getPassword(String token){
        try {
            // 使用JwtHelper工具类获取token中的用户密码
            String password = JwtHelper.getPassword(token);
            return password;
        } catch (ExpiredJwtException e) {
            throw new TmallException(ResultCodeEnum.LOGIN_AUTH);
        }
    }

    /**
     * 获取token中的用户ID
     * @param request
     * @return
     */
    public static String getUserId(HttpServletRequest request){
        try {
            // 获取请求头中的token
            String token = request.getHeader("token");
            // 使用JwtHelper工具类获取token中的用户ID
            String userId = JwtHelper.getUserId(token);
            return userId;
        } catch (ExpiredJwtException e) {
            throw new TmallException(ResultCodeEnum.LOGIN_AUTH);
        }
    }

    /**
     * 获取token中的用户ID
     * @param token
     * @return
     */
    public static String getUserId(String token){
        try {
            // 使用JwtHelper工具类获取token中的用户ID
            String userId = JwtHelper.getUserId(token);
            return userId;
        } catch (ExpiredJwtException e) {
            throw new TmallException(ResultCodeEnum.LOGIN_AUTH);
        }
    }
}
