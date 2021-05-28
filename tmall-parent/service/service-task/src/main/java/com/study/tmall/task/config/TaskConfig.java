package com.study.tmall.task.config;

import com.study.tmall.dto.TimerTask;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:15
 * Versions:1.0.0
 * Description:
 */
@Configuration
public class TaskConfig {
    // 计时器任务队列（ConcurrentLinkedQueue是解决高并发的队列）
    public final static ConcurrentLinkedQueue<TimerTask> taskQueue = new ConcurrentLinkedQueue();
}
