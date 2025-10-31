package com.example.haustoj.service.user;

import com.example.haustoj.pojo.po.Auth;

import java.util.List;

public interface RoleAuthService {
    List<Auth> getAuthsByRoleId(Long roleId);
}
