package com.study.tmall.task.listener;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.task.config.TaskConfig;
import com.study.tmall.task.handler.MySource;
import com.study.tmall.task.util.ConstantPropertyUtil;
import com.study.tmall.task.util.TaskQueueUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Order(value = 2) // 在application启动完成并且taskQueue初始化后启动
public class TaskListener implements ApplicationRunner {
    private static ConcurrentLinkedQueue<TimerTask> payTaskQueue;
    private static ConcurrentLinkedQueue<TimerTask> reviewTaskQueue;

    @Resource
    private MessageChannel timerPayTaskSend;
    @Resource
    private MessageChannel timerReviewTaskSend;

    /**
     * 支付倒计时线程启动方法
     */
    public void payLeftTimer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000); // 每隔一秒扫描一次
                        payTaskQueue.stream().forEach(item -> {
                            // 如果开始时间晚于结束时间，那么表示该任务可以执行了
                            long nowTime = System.currentTimeMillis();
                            if (nowTime > item.getExecuteTime()) {
                                // 把任务发送到rabbitMQ中
                                timerPayTaskSend.send(MessageBuilder.withPayload(item).build());
                                System.out.println("计时器支付任务执行了：" + item.getData());
                                payTaskQueue.remove(item); // 从队列中移除该任务
                                // 更新序列化文件
                                TaskQueueUtil.writeTaskQueue(payTaskQueue, ConstantPropertyUtil.PAY_TASK_FILE_PATH);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setName("timerPayTaskThread");
        thread.setDaemon(true); // 设置为守护线程
        thread.start();
    }

    /**
     * 评价倒计时线程启动方法
     */
    public void reviewLeftTimer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000); // 每隔一秒扫描一次
                        reviewTaskQueue.stream().forEach(item -> {
                            // 如果开始时间晚于结束时间，那么表示该任务可以执行了
                            long nowTime = System.currentTimeMillis();
                            if (nowTime > item.getExecuteTime()) {
                                // 把任务发送到rabbitMQ中
                                timerReviewTaskSend.send(MessageBuilder.withPayload(item).build());
                                System.out.println("计时器评价任务执行了：" + item.getData());
                                reviewTaskQueue.remove(item); // 从队列中移除该任务
                                // 更新序列化文件
                                TaskQueueUtil.writeTaskQueue(reviewTaskQueue, ConstantPropertyUtil.REVIEW_TASK_FILE_PATH);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setName("timerReviewTaskThread");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 在application启动完成并且taskQueue初始化后开启倒计时线程
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        payTaskQueue = TaskConfig.getPayTaskQueue(); // 获取支付任务队列
        reviewTaskQueue = TaskConfig.getReviewTaskQueue(); // 获取评价任务队列
        // 开启支付倒计时线程
        this.payLeftTimer();
        // 开启评价倒计时线程
        this.reviewLeftTimer();
    }
}
