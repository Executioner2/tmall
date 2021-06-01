package com.study.tmall.task.api;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.task.config.TaskConfig;
import com.study.tmall.task.util.ConstantPropertyUtil;
import com.study.tmall.task.util.TaskQueueUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 20:19
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "定时器任务接口")
@RestController
@RequestMapping("/api/task/timerTask")
public class TimerTaskApi {

    @ApiOperation("增加支付任务（这个接口只用添加任务）")
    @PostMapping("/inner/add/payTask")
    public void addPayTask(
            @ApiParam(name = "task", value = "定时任务", required = true)
            @RequestBody TimerTask task) {

        ConcurrentLinkedQueue<TimerTask> payTaskQueue = TaskConfig.getPayTaskQueue();
        // 添加定时任务
        payTaskQueue.add(task);
        // 保存到支付任务序列化文件
        TaskQueueUtil.writeTaskQueue(payTaskQueue, ConstantPropertyUtil.PAY_TASK_FILE_PATH);
    }

    @ApiOperation("增加评价任务（这个接口只用添加任务）")
    @PostMapping("/inner/add/reviewTask")
    public void addReviewTask(
            @ApiParam(name = "task", value = "定时任务", required = true)
            @RequestBody TimerTask task) {

        ConcurrentLinkedQueue<TimerTask> reviewTaskQueue = TaskConfig.getReviewTaskQueue();
        // 添加定时任务
        reviewTaskQueue.add(task);
        // 保存到评价任务序列化文件
        TaskQueueUtil.writeTaskQueue(reviewTaskQueue, ConstantPropertyUtil.REVIEW_TASK_FILE_PATH);
    }
}
