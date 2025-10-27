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
@TableName("user") // 指定表名
public class User {
    @TableId(type = IdType.AUTO) // 如果有id字段且是自增主键
    private Long id;
    private String email ;
    private String username ;
    private String password ;
    private Integer status ;
    private Date createTime ;
    private Date updateTime ;


}
