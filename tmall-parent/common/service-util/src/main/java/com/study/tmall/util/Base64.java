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

    static {
        // 对盐进行加密
        slotEncrypt = MD5.base64SlotEncrypt(base64Slot);
    }

    private Base64() {}

    public static String getAccount(String account) {
        org.apache.tomcat.util.codec.binary.Base64 base64 = new org.apache.tomcat.util.codec.binary.Base64();
        account = new String(base64.decode(account));
        account = account.substring(0, account.length() - slotEncrypt.length());
        account = new String(base64.decode(account));
        return account;
    }
}
