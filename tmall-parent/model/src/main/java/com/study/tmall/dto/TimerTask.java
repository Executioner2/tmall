package com.study.tmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-28 19:44
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "计时器任务传输对象")
public class TimerTask<T> {
    @ApiModelProperty("结束时间，当前时间(ms)+到执行时间的等待时间(ms)")
    private Long executeTime;

    @ApiModelProperty("携带的数据")
    private T data;
}
