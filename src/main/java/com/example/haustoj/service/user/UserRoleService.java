package com.example.haustoj.service.user;

import javax.annotation.Resource;

import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.vo.UserRolesVo;

import java.util.List;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
public interface UserRoleService {
    public List<Role> getRolesByUid(String uid);
}
