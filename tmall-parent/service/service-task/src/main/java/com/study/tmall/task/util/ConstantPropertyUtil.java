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
    @Value("${file.save.path.pay-task}")
    private String payTaskFilePath;

    @Value("${file.save.path.review-task}")
    private String reviewTaskFilePath;

    public static String PAY_TASK_FILE_PATH;
    public static String REVIEW_TASK_FILE_PATH;

    @Override
    public void afterPropertiesSet() throws Exception {
        PAY_TASK_FILE_PATH = payTaskFilePath;
        REVIEW_TASK_FILE_PATH = reviewTaskFilePath;
    }
}
