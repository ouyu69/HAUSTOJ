package com.example.haustoj.service.user.Impl;

import com.example.haustoj.manager.UserManager;
import com.example.haustoj.pojo.po.User;
import com.example.haustoj.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @FileName UserServiceImpl
 * @Description
 * @Author ouyu
 * @Date 2025-10-24
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserManager userManager;
    @Override
    public User getUserInfoByUid(String uid) {
        return userManager.getUserInfoByUid(uid);
    }
}
