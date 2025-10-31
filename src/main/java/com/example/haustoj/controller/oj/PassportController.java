package com.example.haustoj.controller.oj;

import  com.example.haustoj.annocation.AnonApi;
import com.example.haustoj.common.CommonResult;
import com.example.haustoj.controller.ABaseController;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.pojo.dto.RegisterDto;
import com.example.haustoj.pojo.vo.RegisterCodeVO;
import com.example.haustoj.pojo.vo.UserInfoVo;
import com.example.haustoj.service.oj.PassportService;
import com.example.haustoj.utils.Constants;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-29 13:35
 **/
@RestController
@RequestMapping("/passport")
public class PassportController extends ABaseController {
    @Resource
    private PassportService passportService;
    @PostMapping("/login")
    @AnonApi
    public CommonResult<UserInfoVo> login(@RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        return passportService.login(loginDto, response, request);
    }
    @PostMapping("/register")
    @AnonApi
    public CommonResult<UserInfoVo> register(@RequestBody RegisterDto  registerDto) {
        return passportService.register(registerDto, Constants.Roles.USER.getCode());
    }
    @PostMapping("/get-register-code")
    @AnonApi
    public CommonResult<RegisterCodeVO> getRegisterCode(@Validated @RequestBody RegisterDto registerDto) {
        return passportService.getRegisterCode(registerDto.getEmail());
    }
}
