package com.example.haustoj.common.exception;

import com.example.haustoj.common.ResultStatus;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-28 19:24
 **/
public class BusinessException extends Exception {
    private ResultStatus resultStatus;
    private Integer code;
    private String msg;

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.msg = message;
    }

    public BusinessException(String message) {
        super(message);
        this.msg = message;
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(ResultStatus codeEnum) {
        super(codeEnum.getMsg());
        this.resultStatus = codeEnum;
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }

    public BusinessException(String msg, Integer code) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public BusinessException(Throwable cause, ResultStatus resultStatus, Integer code, String msg) {
        super(cause);
        this.resultStatus = resultStatus;
        this.code = code;
        this.msg = msg;
    }


    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
