package com.example.haustoj.advice;

import com.example.haustoj.common.CommonResult;
import com.example.haustoj.common.ResultStatus;
import com.example.haustoj.common.exception.BusinessException;
import com.example.haustoj.common.exception.StatusFailException;
import com.example.haustoj.controller.ABaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.lang.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.BindException;

/**
 * @Description 全局异常处理
 * @Author ouyu
 * @Date 2025-10-28 19:09
 **/
@Slf4j(topic= "haustoj")
@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);
    @ExceptionHandler(value = {Exception.class})
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误,请求地址{},错误信息", request.getRequestURL(), e);
        CommonResult ajaxResponse ;
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse = CommonResult.errorResponse(ResultStatus.NOT_FOUND);
        } else if (e instanceof BusinessException) {
            ajaxResponse = CommonResult.errorResponse(e.getMessage(), ResultStatus.FAIL);
        } else {
            ajaxResponse = CommonResult.errorResponse(e.getMessage() == null ? ResultStatus.SYSTEM_ERROR.getMsg() : e.getMessage(),ResultStatus.SYSTEM_ERROR.getCode());
        }
        return ajaxResponse;
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = ShiroException.class)
    public CommonResult<Void> handleShiroException(ShiroException e,
                                                   HttpServletRequest httpRequest,
                                                   HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return CommonResult.errorResponse("对不起，您无权限进行此操作，请先登录进行授权认证", ResultStatus.FORBIDDEN);
    }
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public CommonResult<String> unauthorizedException(Exception ex){
        return CommonResult.errorResponse(ex.getMessage(), ResultStatus.FORBIDDEN);
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AuthorizationException.class)
    public CommonResult<String> handleAuthenticationException(AuthorizationException e,
                                                              HttpServletRequest httpRequest,
                                                              HttpServletResponse httpResponse) {
        httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
        return CommonResult.errorResponse("对不起，您无权限进行此操作！" + e);
    }



}
