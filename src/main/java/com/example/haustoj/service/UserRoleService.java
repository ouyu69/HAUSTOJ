package com.example.haustoj.service;

import javax.annotation.Resource;

import com.example.haustoj.pojo.vo.UserRolesVo;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
public interface UserRoleService {
    public UserRolesVo getUserRoles(String userId, String email);

}
