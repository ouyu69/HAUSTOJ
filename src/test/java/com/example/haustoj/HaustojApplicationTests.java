package com.example.haustoj;

import com.example.haustoj.dao.UserDao;
import com.example.haustoj.dao.UserRoleDao;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.po.User;
import com.example.haustoj.pojo.vo.UserRolesVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import javax.annotation.Resource;

@SpringBootTest
class HaustojApplicationTests {
    @Resource
    UserDao userDao ;
    @Resource
    UserRoleDao userRoleDao ;
    @Test
    void contextLoads() {
    }
    @Test
    void saveUser(){
        User user = new User();
        user.setEmail("<EMAIL>");
        user.setUsername("ouyu");
        user.setPassword("<PASSWORD>");
        user.setStatus(1);
        userDao.insert( user);
    }
    @Test
    void userRoleTest(){
        String email = "<EMAIL>" ;
        UserRolesVo userRoles = userRoleDao.getUserRoles(null, email);
        for(Role role : userRoles.getRoleList()){
            System.out.println(role);
        }
    }

}
