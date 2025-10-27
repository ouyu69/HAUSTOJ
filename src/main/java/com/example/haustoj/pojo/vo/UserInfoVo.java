package com.example.haustoj.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
@Data
public class UserInfoVo {
    private Long userId ;
    private String email ;
    private String username ;
    private List<String> RoleList ;
    
}
