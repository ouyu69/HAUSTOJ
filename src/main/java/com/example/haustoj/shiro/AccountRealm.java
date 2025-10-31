package com.example.haustoj.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.example.haustoj.pojo.po.Auth;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.po.User;
import com.example.haustoj.service.user.RoleAuthService;
import com.example.haustoj.service.user.UserRoleService;
import com.example.haustoj.service.user.UserService;
import com.example.haustoj.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.example.haustoj.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @FileName AccountRealm
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Slf4j
@Component
public class AccountRealm  extends AuthorizingRealm {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleAuthService roleAuthService;
    @Resource
    UserService userService;

    /**
     * 获取用户权限信息，执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AccountProfile accountProfile = (AccountProfile) principalCollection.getPrimaryPrincipal();
        // 角色权限表
        List<String> permissionsNameList= new LinkedList<>();
        // 用户角色表
        List<String> roleNameList= new LinkedList<>();
        List<Role> roleList = userRoleService.getRolesByUid(accountProfile.getId());
        roleNameList = roleList.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        for(Role role : roleList){
            for(Auth auth : roleAuthService.getAuthsByRoleId(role.getId())){
                permissionsNameList.add(auth.getPermission());
            }
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roleNameList);
        authorizationInfo.addStringPermissions(permissionsNameList);
        return authorizationInfo;
    }
    /**
     * 获取用户认证信息,执行认证逻辑
     * @param jwtToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken jwtToken) throws AuthenticationException {
        JwtToken jwt = (JwtToken) jwtToken;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getUserInfoByUid(userId);
        if(user == null){
            throw new UnknownAccountException("用户不存在");
        } else if(!Constants.UserStatus.NORMAL.getCode().equals(user.getStatus())){
            throw new LockedAccountException("用户已被封禁或被删除，请联系管理员处理！");
        }
        AccountProfile accountProfile = new AccountProfile();
        BeanUtil.copyProperties(user, accountProfile);
        accountProfile.setUuid(userId);
        return new SimpleAuthenticationInfo(accountProfile, jwt.getCredentials(), getName());
    }
    @Override
    public boolean supports(AuthenticationToken token) {
//        log.info("Checking token support: {}", token.getClass().getName());
        return token instanceof JwtToken;
    }
}
