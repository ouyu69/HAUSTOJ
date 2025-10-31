package com.example.haustoj.controller;

import com.example.haustoj.common.CommonResult;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-28 19:15
 **/
public class ABaseController {

    protected <T> CommonResult<T> getSuccessCommonResult(T t) {
        return CommonResult.successResponse(t);
    }
}
