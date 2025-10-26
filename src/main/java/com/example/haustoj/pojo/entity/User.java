package com.example.haustoj.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @FileName User
 * @Description
 * @Author ouyu
 * @Date 2025-10-24
 **/
@Data
@TableName("user") // 指定表名
public class User {
    @TableId(type = IdType.AUTO) // 如果有id字段且是自增主键
    Long id;
    String email ;
    String username ;
    String password ;
    Integer status ;


}
