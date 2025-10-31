package com.example.haustoj.validator;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import com.example.haustoj.common.exception.StatusFailException;
import com.example.haustoj.config.NacosSwitchConfig;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.pojo.dto.RegisterDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-30 15:08
 **/
@Component
public class PassportValidator {
    @Resource
    NacosSwitchConfig nacosSwitchConfig;
    public void validateLogin(LoginDto loginDto) throws StatusFailException {
        if (loginDto.getUsername() == null || loginDto.getUsername().isEmpty()) {
            throw new StatusFailException("用户名不能为空");
        }
        if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            throw new StatusFailException("密码不能为空");
        }
    }
    public void validateRegister(RegisterDto registerDto) throws StatusFailException {
        if (registerDto.getEmail() == null || registerDto.getEmail().isEmpty()) {
            throw new StatusFailException("邮箱不能为空！");
        }
        if(!Validator.isEmail(registerDto.getEmail())){
            throw new StatusFailException("邮箱格式错误！");
        }
        if (registerDto.getUsername() == null || registerDto.getUsername().isEmpty()) {
            throw new StatusFailException("用户名不能为空！");
        }
        if (registerDto.getUsername().startsWith(" ") || registerDto.getUsername().endsWith(" ")) {
            throw new StatusFailException("用户名不能以空格开头或结尾！");
        }
        if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
            throw new StatusFailException("密码不能为空！");
        }
        if (registerDto.getPassword().startsWith(" ") || registerDto.getPassword().endsWith(" ")) {
            throw new StatusFailException("密码不能以空格开头或结尾！");
        }
        String passwordRegex = "^[^\\\\s]{6,20}$";
        if(!registerDto.getPassword().matches(passwordRegex)){
            throw new StatusFailException("密码格式错误！");
        }
    }
}
