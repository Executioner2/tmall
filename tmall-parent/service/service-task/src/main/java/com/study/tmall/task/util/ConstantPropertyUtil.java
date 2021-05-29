package com.study.tmall.task.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 18:32
 * Versions:1.0.0
 * Description:
 */
@Component
public class ConstantPropertyUtil implements InitializingBean {
    @Value("${file.save.path}")
    private String filePath;

    public static String FILE_PATH;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("赋值开始");
        FILE_PATH = filePath;
    }
}
