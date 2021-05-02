package com.study.tmall.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 18:00
 * Versions:1.0.0
 * Description: MD5
 */
public final class MD5 {
    /**
     * MD5加盐转码
     * @param strSrc
     * @return
     */
    public static String encrypt(String strSrc) {
        try {
            char hexChars[] = { 'f', 'e', 'd', 'c', 'b', 'a', '9', '8', '7',
                    '6', '5', '4', '3', '2', '1', '0' };
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }


    /**
     * MD5加盐转码（对base64编码中的盐加密）
     * @param strSrc
     * @return
     */
    public static String base64SlotEncrypt(String strSrc) {
        try {
            char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            // 加盐
            String strChars = new String(chars);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < strChars.length(); i++) {
                // 转换为二进制
                String context = Integer.toBinaryString(strChars.charAt(i));
                Integer binary = Integer.valueOf(context);
                // 根据位运算从盐中选取颗粒
                result.append(hexChars[binary >>> 4 & 0xf]);
                result.append(hexChars[binary & 0xf]);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }
}
