package com.study.tmall.util;

import com.study.tmall.enums.ImageUrlEnum;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 22:45
 * Versions:1.0.0
 * Description: 图片工具类
 */
public class ImageUtil {

    /**
     * 传入保存图片后fastUtil返回的字符数组
     * 返回融合后的图片url地址
     * @param fileArray
     * @return
     */
    public static String compoundUrl(String[] fileArray){
        // 最终访问地址
        // http://nginx的Ip/组名/存储路径
        // imageUrl = "http://192.168.123.130/" + fileArray[0] + "/" + fileArray[1]
        // http://192.168.123.130/group1/M00/00/00/wKh7gmBrTfeEAyKkAAAAAFQAsAw989.jpg
        String url = fileArray == null ? "" : ImageUrlEnum.NGINX_IP.getIp() + fileArray[0] + "/" + fileArray[1];

        return url;
    }
}
