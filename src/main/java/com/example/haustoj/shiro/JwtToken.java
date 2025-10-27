package com.example.haustoj.shiro;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * @FileName JwtToken
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
public class JwtToken implements AuthenticationToken{
    String token;

    public JwtToken(String token) {
        this.token = token;
    }

    
    /** 
     * @return Object
     */
    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
