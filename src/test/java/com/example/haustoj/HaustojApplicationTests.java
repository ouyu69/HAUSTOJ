package com.example.haustoj;

import com.example.haustoj.config.NacosSwitchConfig;
import com.example.haustoj.config.WebConfig;
import com.example.haustoj.dao.UserDao;
import com.example.haustoj.dao.UserRoleDao;
import com.example.haustoj.manager.email.EmailManager;
import com.example.haustoj.pojo.po.Auth;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.po.User;
import com.example.haustoj.pojo.vo.UserRolesVo;
import com.example.haustoj.service.user.RoleAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

import javax.annotation.Resource;

@SpringBootTest
class HaustojApplicationTests {
    @Resource
    UserDao userDao ;
    @Resource
    UserRoleDao userRoleDao ;
    @Resource
    RoleAuthService roleAuthService ;
    @Resource
    EmailManager emailManager ;
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
        String username = "ouyu" ;
        UserRolesVo userRoles = userRoleDao.getUserRoles(null, username);
        System.out.println(userRoles);
    }
    @Resource
    NacosSwitchConfig nacosSwitchConfig ;
    @Test
    void publishWebConfig(){
        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        webConfig.setDescription("Hello Haust");
        if (nacosSwitchConfig.publishWebConfig(webConfig)){
            System.out.println("发布成功");
        }else{
            System.out.println("发布失败");
        }
    }
    @Test
    void getWebConfig(){
        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        System.out.println(webConfig.getBaseUrl());
    }
    @Test
    void getAuthsByRoleId(){
        List<Auth> list = roleAuthService.getAuthsByRoleId(0L);
        for(Auth a : list){
            System.out.println(a);
        }
    }
    @Test
    void sendRegisterCode(){
    }

}
