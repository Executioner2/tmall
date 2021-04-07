package com.study.tmall.exception;

import com.study.tmall.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 16:57
 * Versions:1.0.0
 * Description: 项目自定义异常
 */
@Data
@ApiModel(description = "自定义全局异常类")
public class TmallException extends RuntimeException{
    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和消息创建异常对象
     * @param code
     * @param message
     */
    public TmallException(Integer code, String message){
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举对象
     * @param resultCodeEnum
     */
    public TmallException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
