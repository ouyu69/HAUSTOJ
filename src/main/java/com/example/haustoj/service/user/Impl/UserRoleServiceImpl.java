package com.example.haustoj.service.user.Impl;

import com.example.haustoj.dao.UserRoleDao;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.service.user.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:43
 * @Description: 
 */
@Service
public class UserRoleServiceImpl implements UserRoleService{
    @Resource
    private UserRoleDao userRoleDao;
    @Override
    public List<Role> getRolesByUid(String uid) {
        return userRoleDao.getRolesByUid(uid);
    }
}
