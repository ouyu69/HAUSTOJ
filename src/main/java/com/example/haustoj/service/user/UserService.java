package com.example.haustoj.service.user;

import com.example.haustoj.pojo.po.User;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
public interface UserService{
    User getUserInfoByUid(String uid);
    
}
