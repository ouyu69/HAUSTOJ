package com.example.haustoj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.po.UserRole;
import com.example.haustoj.pojo.vo.UserRolesVo;
import org.springframework.stereotype.Component;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:16
 * @Description:
 */
@Component
public interface UserRoleDao extends BaseMapper<UserRole>{
    UserRolesVo getUserRoles(@Param("uid") String uid, @Param("username") String username);
    List<Role> getRolesByUid(@Param("uid") String uid);
}
