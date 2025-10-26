package com.example.haustoj.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.example.haustoj.utils.JwtUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    /**
     * 获取用户权限信息，执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
    /**
     * 获取用户认证信息,执行认证逻辑
     * @param jwtToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken jwtToken) throws AuthenticationException {
        return null;
    }
}
