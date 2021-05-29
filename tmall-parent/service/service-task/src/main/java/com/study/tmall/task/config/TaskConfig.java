package com.study.tmall.task.config;

import com.study.tmall.dto.TimerTask;
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
    private static ConcurrentLinkedQueue<TimerTask> taskQueue;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskQueue = TaskQueueUtil.readTaskQueue();
    }

    public static ConcurrentLinkedQueue<TimerTask> getTaskQueue() {
        System.out.println("获取的队列：" + taskQueue);
        return taskQueue;
    }
}
