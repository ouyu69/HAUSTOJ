package com.example.haustoj.pojo.vo;

import java.util.Date;
import java.util.List;

import com.example.haustoj.pojo.po.Role;

import lombok.Data;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:40
 * @Description:
 */
@Data
public class UserRolesVo {
   private String uid ;
   private String email ;
   private String username ;
   private String password ;
   private Integer status ;
   private Date createTime ;
   private Date updateTime ;
   private List<Role> roleList ;
}
