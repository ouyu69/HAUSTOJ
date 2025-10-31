package com.example.haustoj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.haustoj.pojo.po.Auth;
import com.example.haustoj.pojo.po.RoleAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleAuthDao extends BaseMapper<RoleAuth> {
    List<Auth> getAuthsByRoleId(@Param("roleId") Long roleId);
}
