package com.example.haustoj.common;

import lombok.Data;

/**
 * @FileName CommonResult
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Data
public class CommonResult<T> {
    private String message;
    private Integer code;
    private T data;

    public CommonResult(Integer status, T data, String msg) {
        this.code = status;
        this.data = data;
        this.message = msg;
    }

    /**
     * 成功的结果
     *
     * @param data 返回结果
     * @param msg  返回信息
     */
    public static <T> CommonResult<T> successResponse(T data, String msg) {
        return new CommonResult<>(ResultStatus.SUCCESS.getCode(), data, msg);
    }
    /**
     * 成功的结果
     *
     * @param data 返回结果
     */
    public static <T> CommonResult<T> successResponse(T data) {
        return new CommonResult<T>(ResultStatus.SUCCESS.getCode(), data, "success");
    }

    /**
     * 成功的结果
     *
     * @param msg 返回信息
     */
    public static <T> CommonResult<T> successResponse(String msg) {
        return new CommonResult<T>(ResultStatus.SUCCESS.getCode(), null, msg);
    }

    /**
     * 成功的结果
     */
    public static <T> CommonResult<T> successResponse() {
        return new CommonResult<T>(ResultStatus.SUCCESS.getCode(), null, "success");
    }


    /**
     * 失败的结果，无异常
     *
     * @param msg 返回信息
     */
    public static <T> CommonResult<T> errorResponse(String msg) {
        return new CommonResult<T>(ResultStatus.FAIL.getCode(), null, msg);
    }

    public static <T> CommonResult<T> errorResponse(ResultStatus resultStatus) {
        return new CommonResult<T>(resultStatus.getCode(), null, resultStatus.getMsg());
    }

    public static <T> CommonResult<T> errorResponse(String msg, ResultStatus resultStatus) {
        return new CommonResult<T>(resultStatus.getCode(), null, msg);
    }

    public static <T> CommonResult<T> errorResponse(String msg, Integer status) {
        return new CommonResult<T>(status, null, msg);
    }
}
