package com.example.haustoj.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
@Data
public class UserInfoVo {
    private String uid ;
    private String email ;
    private String username ;
    private Integer status ;
    private Set<String> RoleSet ;
    private Set<String> AuthSet ;
    
}
