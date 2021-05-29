package com.study.tmall.task.listener;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.task.config.TaskConfig;
import com.study.tmall.task.handler.MySource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:17
 * Versions:1.0.0
 * Description:
 */
@Component
@EnableBinding(MySource.class)
public class TaskListener {
    private final static ConcurrentLinkedQueue<TimerTask> taskQueue = TaskConfig.taskQueue;

    @Resource
    private MessageChannel timerTaskSend;

    @Async
    @PostConstruct // 在application启动时一起启动
    public void leftTimer() { // 倒计时线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000); // 每隔一秒扫描一次
                        taskQueue.stream().forEach(item -> {
                            // 如果开始时间晚于结束时间，那么表示该任务可以执行了
                            long nowTime = System.currentTimeMillis();
                            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            if (nowTime - item.getExecuteTime() > 0) {
                                // TODO 把任务发送到rabbitMQ中
                                timerTaskSend.send(MessageBuilder.withPayload(item).build());
                                System.out.println("定时任务执行了：" + item.getData());
                                taskQueue.remove(item); // 从队列中移除该任务
                                System.out.println("删除任务后的队列：" + taskQueue);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setName("timerTaskThread");
        thread.setDaemon(true); // 设置为守护线程
        thread.start();
    }
}
