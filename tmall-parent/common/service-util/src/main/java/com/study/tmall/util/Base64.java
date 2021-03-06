package com.study.tmall.util;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-02 16:22
 * Versions:1.0.0
 * Description: 对前端传来的account进行解码
 */
public class Base64 {
    public static final String base64Slot = "2Executioner";
    private static final String slotEncrypt;
    private static final org.apache.tomcat.util.codec.binary.Base64 base64;

    static {
        // 对盐进行加密
        slotEncrypt = MD5.base64SlotEncrypt(base64Slot);
        base64 = new org.apache.tomcat.util.codec.binary.Base64();
    }

    private Base64() {}

    /**
     * base64编码
     * @param data
     * @return
     */
    public static String encode(String data) {
        data = new String(base64.encode(data.getBytes()));
        data += slotEncrypt;
        data = new String(base64.encode(data.getBytes()));
        return data;
    }

    /**
     * 对前端的Base64编码进行解码
     * @param data
     * @return
     */
    public static String decode(String data) {
        data = new String(base64.decode(data));
        data = data.substring(0, data.length() - slotEncrypt.length());
        data = new String(base64.decode(data));
        return data;
    }
}
