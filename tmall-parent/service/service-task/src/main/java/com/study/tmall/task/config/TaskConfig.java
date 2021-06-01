package com.study.tmall.task.config;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.task.util.ConstantPropertyUtil;
import com.study.tmall.task.util.TaskQueueUtil;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:15
 * Versions:1.0.0
 * Description:
 */
@Configuration
@Order(value = 1)
public class TaskConfig implements ApplicationRunner {
    // 计时器任务队列（ConcurrentLinkedQueue是解决高并发的队列）
    // 支付任务队列
    private static ConcurrentLinkedQueue<TimerTask> payTaskQueue;
    // 退货任务队列
    private static ConcurrentLinkedQueue<TimerTask> reviewTaskQueue;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 读取支付任务队列文件，取得支付任务队列中的数据
        payTaskQueue = TaskQueueUtil.readTaskQueue(ConstantPropertyUtil.PAY_TASK_FILE_PATH);
        // 读取评价任务队列文件，取得评价任务队列中的数据
        reviewTaskQueue = TaskQueueUtil.readTaskQueue(ConstantPropertyUtil.REVIEW_TASK_FILE_PATH);
    }

    public static ConcurrentLinkedQueue<TimerTask> getPayTaskQueue() {
        return payTaskQueue;
    }

    public static ConcurrentLinkedQueue<TimerTask> getReviewTaskQueue() {
        return reviewTaskQueue;
    }
}
