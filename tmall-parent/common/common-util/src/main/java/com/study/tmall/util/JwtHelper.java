package com.study.tmall.util;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 16:40
 * Versions:1.0.0
 * Description: 使用 jwt 把登录信息做一个base64转码使其变成token
 */
public class JwtHelper {
    private static long tokenExpiration = 24*60*60*1000;  // token过期时间
    private static String tokenSignKey = "123456";  // token 签名密钥，可以自己设置

    private JwtHelper(){}
    /**
     * 根据参数生成token
     * @param userId
     * @param password
     * @return
     */
    public static String createToken(String userId, String password) {
        String token = Jwts.builder()
                .setSubject("TMALL-USER") // 组名
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // 设置token过期时间，毫秒单位
                .claim("userId", userId)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * 根据token取出userId
     * @param token
     * @return
     */
    public static String getUserId(String token) throws ExpiredJwtException{
        if(StringUtils.isEmpty(token)) return "";
        String userId = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            userId = (String) claims.get("userId");
        } catch (ExpiredJwtException e) {
            // token过期
            System.out.println("token过期：" + e);
            throw e;
        }
        return userId;
    }

    /**
     * 根据token取出password
     * @param token
     * @return
     */
    public static String getPassword(String token) {
        if(StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("password");
    }
}
