package com.study.tmall.util;

import com.study.tmall.enums.ImageUrlEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 22:45
 * Versions:1.0.0
 * Description: 图片工具类
 */
public class ImageUtil {

    private ImageUtil(){}

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
        String url = fileArray == null ? "" : ImageUrlEnum.NGINX_IP.getStr() + "/" + fileArray[0] + "/" + fileArray[1];

        return url;
    }

    /**
     * 拆分数据库中存入的图片url
     * @param imageUrl
     * @return
     */
    public static String[] splitUrl(String imageUrl){
        // imageUrl = http://192.168.123.130/group1/M00/00/00/wKh7gmBrTfeEAyKkAAAAAFQAsAw989.jpg
        // str[0] = http://192.168.123.130
        // str[1] = group1
        // str[2] = M00/00/00/wKh7gmBrTfeEAyKkAAAAAFQAsAw989.jpg

        int ipLength = ImageUrlEnum.NGINX_IP.getStr().length();
        int groupLength = ImageUrlEnum.GROUP_1.getStr().length();
        String[] str = new String[3];
        str[0] = imageUrl.substring(0, ipLength);
        str[1] = imageUrl.substring(ipLength + 1, ipLength + groupLength + 1);
        str[2] = imageUrl.substring(ipLength + groupLength + 2);
        return str;
    }

    /**
     * 获得文件后缀名
     * @param file
     * @return
     */
    public static String getFileExtName(MultipartFile file){
        String filename = file.getOriginalFilename(); // 获得文件名
        String fileExtName = "jpg"; // 默认为jpg，防止出错
        if (filename.contains(".")) {
            fileExtName = filename.substring(filename.lastIndexOf(".") + 1); // 取得文件拓展名
        }
        return fileExtName;
    }

    /**
     * 获得文件后缀名
     * @param filename
     * @return
     */
    public static String getFileExtName(String filename){
        String fileExtName = "jpg"; // 默认为jpg，防止出错
        if (filename.contains(".")) {
            fileExtName = filename.substring(filename.lastIndexOf(".") + 1); // 取得文件拓展名
        }
        return fileExtName;
    }
}
