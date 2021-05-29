package com.study.tmall.task.client;

import com.study.tmall.dto.TimerTask;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-29 15:05
 * Versions:1.0.0
 * Description:
 */
@Component
@FeignClient("service-task")
public interface TaskFeignClient {
    // 增加任务（内部调用）
    @PostMapping("/api/task/timerTask/inner/addTask")
    void addTask(@RequestBody TimerTask task);
}
