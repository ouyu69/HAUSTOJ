package com.example.haustoj.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.haustoj.dao.UserDao;
import com.example.haustoj.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-29 19:57
 **/
@Slf4j
@Component
public class UserManager {
    @Resource
    private UserDao userDao;
    public User getUserInfoByUid(String uuid){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid)
                .select("uuid", "email", "username", "status");
        return userDao.selectOne(queryWrapper);
    }
}
