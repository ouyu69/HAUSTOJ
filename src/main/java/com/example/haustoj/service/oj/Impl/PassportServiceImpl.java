package com.example.haustoj.service.oj.Impl;

import com.example.haustoj.common.CommonResult;
import com.example.haustoj.common.exception.StatusFailException;
import com.example.haustoj.manager.oj.PassportManager;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.pojo.dto.RegisterDto;
import com.example.haustoj.pojo.vo.RegisterCodeVO;
import com.example.haustoj.pojo.vo.UserInfoVo;
import com.example.haustoj.service.oj.PassportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-29 13:45
 **/
@Service
public class PassportServiceImpl implements PassportService {
    @Resource
    private PassportManager passportManager;
    @Override
    public CommonResult<UserInfoVo> login(LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        try{
            return CommonResult.successResponse(passportManager.login(loginDto, response, request));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<UserInfoVo> register(RegisterDto registerDto, Integer roleId) {
        try{
            return CommonResult.successResponse(passportManager.register(registerDto, roleId));
        } catch (StatusFailException e) {
           return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<RegisterCodeVO> getRegisterCode(String email) {
        try{
            return CommonResult.successResponse(passportManager.getRegisterCode(email));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
