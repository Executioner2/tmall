package com.study.tmall.task.api;

import com.study.tmall.dto.TimerTask;
import com.study.tmall.task.config.TaskConfig;
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
    @ApiModelProperty("任务队列")
    private final static ConcurrentLinkedQueue<TimerTask> taskQueue = TaskConfig.taskQueue;

    @ApiOperation("增加任务（这个接口只用添加任务）")
    @PostMapping("/inner/addTask")
    public void addTask(
            @ApiParam(name = "task", value = "定时任务", required = true)
            @RequestBody TimerTask task) {

        // 添加定时任务
        taskQueue.add(task);
    }
}
