package com.example.haustoj.manager.oj;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.haustoj.common.exception.StatusFailException;
import com.example.haustoj.config.NacosSwitchConfig;
import com.example.haustoj.config.SwitchConfig;
import com.example.haustoj.config.WebConfig;
import com.example.haustoj.dao.RoleAuthDao;
import com.example.haustoj.dao.UserDao;
import com.example.haustoj.dao.UserRoleDao;
import com.example.haustoj.manager.email.EmailManager;
import com.example.haustoj.pojo.dto.LoginDto;
import com.example.haustoj.pojo.dto.RegisterDto;
import com.example.haustoj.pojo.po.Auth;
import com.example.haustoj.pojo.po.User;
import com.example.haustoj.pojo.po.UserRole;
import com.example.haustoj.pojo.vo.RegisterCodeVO;
import com.example.haustoj.utils.Constants;
import com.example.haustoj.utils.IpUtils;
import com.example.haustoj.utils.JwtUtils;
import com.example.haustoj.utils.RedisUtils;
import com.example.haustoj.validator.PassportValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.haustoj.pojo.vo.UserInfoVo;
import com.example.haustoj.pojo.vo.UserRolesVo;
import com.example.haustoj.pojo.po.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:16
 * @Description:
 */
@Slf4j
@Component
public class PassportManager {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private UserDao userDao;
    @Resource
    private BCryptPasswordEncoder passwordEncoder ;
    @Resource
    RoleAuthDao roleAuthDao ;
    @Resource
    private NacosSwitchConfig nacosSwitchConfig ;
    @Resource
    PassportValidator passportValidator;
    @Resource
    EmailManager emailManager;

    /** 
     * @param loginDto
     * @return UserInfoVO
     * @throws StatusFailException 
     */
    public UserInfoVo login(LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) throws StatusFailException {
        passportValidator.validateLogin(loginDto);
        loginDto.setPassword(loginDto.getPassword().trim());
        loginDto.setUsername(loginDto.getUsername().trim());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ipAddr = IpUtils.getIpAddr(request);
        String key = Constants.Account.TRY_LOGIN_NUM.getCode() + loginDto.getUsername() + ipAddr;
        Integer tryLoginCount = (Integer) redisUtils.get(key);
        if (tryLoginCount != null && tryLoginCount >= 20) {
            throw new StatusFailException("对不起！登录失败次数过多，请于30分钟后重试！");
        }
        UserRolesVo userRolesVo = userRoleDao.getUserRoles(null, loginDto.getUsername());
        if(userRolesVo == null){
            throw new StatusFailException("用户名或密码错误！");
        }
        // TODO 密码加密
        if(!passwordEncoder.matches(loginDto.getPassword(), userRolesVo.getPassword())){
            if(tryLoginCount == null){
                redisUtils.set(key, 1, 30 * 60);
            }else{
                redisUtils.set(key, tryLoginCount + 1, 30 * 60);
            }
            throw new StatusFailException("用户名或密码错误！");
        }
        // 检查用户是否被封禁
        if(userRolesVo.getStatus()!=0){
            throw new StatusFailException("该账户已被封禁，请联系管理员处理！");
        }

        String jwt = jwtUtils.generateToken(userRolesVo.getUid());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Allow-Credentials", "Authorization");
        // TODO 会话存储
        if(tryLoginCount != null){
            redisUtils.del(key);
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setEmail(userRolesVo.getEmail());
        userInfoVo.setUsername(userRolesVo.getUsername());
        userInfoVo.setUid(userRolesVo.getUid());
        List<Role> roleList = userRolesVo.getRoleList();
        userInfoVo.setRoleSet(roleList
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        Set<String> authSet = new HashSet<>();
        for(Role r : roleList){
            for(Auth auth : roleAuthDao.getAuthsByRoleId(r.getId())){
                authSet.add(auth.getPermission());
            }
        }
        userInfoVo.setAuthSet(authSet);
        return userInfoVo;
    }

    /**
     * @param email
     * @return RegisterCodeVO
     * @throws StatusFailException
     * @Description: 获取邮箱注册码
     */
    public RegisterCodeVO getRegisterCode(String email) throws StatusFailException {
        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();
        if(!switchConfig.getOpenRegister()){
            throw new StatusFailException("当前系统不开放注册！");
        }
        if(!Validator.isEmail(email)){
            throw new StatusFailException("邮箱格式错误！");
        }
        String lockKey = Constants.Email.RESET_EMAIL_LOCK.getValue() + email;
        if(redisUtils.hasKey(lockKey)){
            throw new StatusFailException("对不起，您的操作次数过于频繁，请于" + redisUtils.getExpire(lockKey) + "秒后再试！" );
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        if(userDao.exists(queryWrapper)){
            throw new StatusFailException("对不起！" + email + "已被注册！请更换其他邮箱进行注册！");
        }
        String registerKey = Constants.Email.REGISTER_KEY_PREFIX.getValue() + email;
        String code =  RandomUtil.randomNumbers(6);
        //发送邮箱验证码
        emailManager.sendRegisterCode(email, code);
        //60秒内不能重复发送邮件
        redisUtils.set(lockKey, 1, 60);
        // 验证码存入缓存，等待验证
        redisUtils.set(registerKey, code,10 * 60);

        RegisterCodeVO RegisterCodeVO = new RegisterCodeVO();
        RegisterCodeVO.setEmail(email);
        RegisterCodeVO.setExpire(10 * 60);
        //缓存验证码
        return RegisterCodeVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserInfoVo register(RegisterDto registerDto, Integer roleId ) throws StatusFailException {

        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();
        if(!switchConfig.getOpenRegister()){
            throw new StatusFailException("当前系统不开放注册！");
        }
        // 验证注册信息
        passportValidator.validateRegister(registerDto);
        //检验验证码
        String registerKey = Constants.Email.REGISTER_KEY_PREFIX.getValue() + registerDto.getEmail();
        if(!redisUtils.hasKey(registerKey)){
            throw new StatusFailException("验证码不存在或已经过期！");
        }
        String code = (String) redisUtils.get(registerKey);
        if(!code.equals(registerDto.getRegisterCode())){
            throw new StatusFailException("验证码错误！");
        }
        String uuid = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        // 在用户表添加信息
        userDao.insert(user);
        // 在用户角色表添加信息
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUid(user.getUuid());
        userRoleDao.insert(userRole);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setEmail(user.getEmail());
        userInfoVo.setUsername(user.getUsername());
        userInfoVo.setUid(user.getUuid());
        redisUtils.del(registerKey);
        return userInfoVo;
    }

}

