package com.example.haustoj.manager;

import com.example.haustoj.common.exception.StatusFailException;
import com.example.haustoj.dao.UserRoleDao;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.utils.Constants;
import com.example.haustoj.utils.IpUtils;
import com.example.haustoj.utils.JwtUtils;
import com.example.haustoj.utils.RedisUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.haustoj.pojo.vo.UserInfoVo;
import com.example.haustoj.pojo.vo.UserRolesVo;
import com.example.haustoj.pojo.po.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:16
 * @Description:
 */
@Component
public class PassportManager {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserRoleDao userRoleDao;
    
    /** 
     * @param loginDto
     * @return UserInfoVO
     * @throws StatusFailException 
     */
    public UserInfoVo login(LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) throws StatusFailException {
        loginDto.setPassword(loginDto.getPassword().trim());
        loginDto.setEmail(loginDto.getEmail().trim());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ipAddr = IpUtils.getIpAddr(request);
        String key = Constants.Account.TRY_LOGIN_NUM.getCode() + loginDto.getEmail() + ipAddr;
        Integer tryLoginCount = (Integer) redisUtils.get(key);
        if (tryLoginCount != null && tryLoginCount >= 20) {
            throw new StatusFailException("对不起！登录失败次数过多，请于30分钟后重试！");
        }
        UserRolesVo userRolesVo = userRoleDao.getUserRoles(null, loginDto.getEmail());
        if(userRolesVo == null){
            throw new StatusFailException("用户名或密码错误！");
        }
        if(!loginDto.getPassword().equals(userRolesVo.getPassword())){
            if(tryLoginCount == null){
                redisUtils.set(key, 1, 30 * 60);
            }else{
                redisUtils.set(key, tryLoginCount + 1, 30 * 60);
            }
            throw new StatusFailException("用户名或密码错误！");
        }
        if(userRolesVo.getStatus()!=0){
            throw new StatusFailException("该账户已被封禁，请联系管理员处理！");
        }

        String jwt = jwtUtils.generateToken(userRolesVo.getUserId().toString());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Allow-Credentials", "Authorization");
        // TODO 会话存储
        if(tryLoginCount != null){
            redisUtils.del(key);
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setEmail(userRolesVo.getEmail());
        userInfoVo.setUsername(userRolesVo.getUsername());
        userInfoVo.setUserId(userRolesVo.getUserId());
        userInfoVo.setRoleList(userRolesVo.getRoleList()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return userInfoVo;
    }

}
