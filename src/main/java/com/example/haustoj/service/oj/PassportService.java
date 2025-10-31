package com.example.haustoj.service.oj;

import com.example.haustoj.common.CommonResult;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.pojo.dto.RegisterDto;
import com.example.haustoj.pojo.vo.RegisterCodeVO;
import com.example.haustoj.pojo.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PassportService {
    public CommonResult<UserInfoVo> login(LoginDto loginDto, HttpServletResponse response, HttpServletRequest request);
    public CommonResult<UserInfoVo> register(RegisterDto registerDto, Integer roleId);
    public CommonResult<RegisterCodeVO> getRegisterCode(String email);
}
