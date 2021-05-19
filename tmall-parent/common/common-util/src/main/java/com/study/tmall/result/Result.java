package com.study.tmall.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-06 19:58
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "控制层统一返回结果")
public class Result<T> {
    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;


    /**
     * 创建一个Result
     * @param data
     * @param <T>
     * @return
     */
    protected static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if (data != null)
            result.setData(data);
        return result;
    }

    /**
     * 创建一个有返回状态码的Result
     * @param data
     * @param resultCodeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum){
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 创建一个自定义状态码和返回消息的Result
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> build(Integer code, String message){
        Result<T> result = build(null);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> ok(){
        return Result.ok(null);
    }

    /**
     * 返回ok
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(){
        return fail(null);
    }

    /**
     * 返回失败
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(T data){
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

}
