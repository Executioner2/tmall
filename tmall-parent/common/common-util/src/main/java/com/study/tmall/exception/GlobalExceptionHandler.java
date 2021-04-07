package com.study.tmall.exception;

import com.study.tmall.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-07 16:56
 * Versions:1.0.0
 * Description: 全区异常处理
 */
@ControllerAdvice // 全局增强注解
public class GlobalExceptionHandler {
    /**
     * 全局异常处理controller
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        // 捕获异常的语句
        e.printStackTrace();
        // 返回结果返回失败
        return Result.fail();
    }

    /**
     * 自定义全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = TmallException.class)
    @ResponseBody
    public Result error(TmallException e){
        // 返回自定义异常
        return Result.build(e.getCode(), e.getMessage());
    }
}
