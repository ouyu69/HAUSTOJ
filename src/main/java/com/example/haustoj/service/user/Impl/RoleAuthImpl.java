package com.example.haustoj.service.user.Impl;

import com.example.haustoj.dao.RoleAuthDao;
import com.example.haustoj.pojo.po.Auth;
import com.example.haustoj.service.user.RoleAuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-29 20:18
 **/
@Service
public class RoleAuthImpl implements RoleAuthService {
    @Resource
    private RoleAuthDao roleAuthDao;
    @Override
    public List<Auth> getAuthsByRoleId(Long roleId) {
        return roleAuthDao.getAuthsByRoleId(roleId);
    }
}
