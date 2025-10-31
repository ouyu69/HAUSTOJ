package com.example.haustoj.pojo.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 17:49
 * @Description:
 */
@Data
@TableName("user_role")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Long id ;
    private String uid ;
    private Integer roleId ;
    private Date createTime ;
    private Date updateTime ;
}
