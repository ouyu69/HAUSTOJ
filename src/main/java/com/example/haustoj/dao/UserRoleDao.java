package com.example.haustoj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.haustoj.pojo.po.Role;
import com.example.haustoj.pojo.po.UserRole;
import com.example.haustoj.pojo.vo.UserRolesVo;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:16
 * @Description:
 */
public interface UserRoleDao extends BaseMapper<UserRole>{
    UserRolesVo getUserRoles(@Param("userId") Long userId, @Param("email") String email);
}
