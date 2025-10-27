package com.example.haustoj.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:39
 * @Description:
 */
@Data
@TableName("role")
public class Role {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long id ;
    private String name ;
    private String description ;
    private Integer status ;
    private Date  createTime ;
    private Date updateTime ;

}
