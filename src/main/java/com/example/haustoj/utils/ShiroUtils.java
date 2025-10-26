package com.example.haustoj.utils;

import com.example.haustoj.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
public class ShiroUtils {

    private ShiroUtils() {
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
