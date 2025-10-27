package com.example.haustoj.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:16
 * @Description:
 */
@Data
@TableName("Auth")
public class Auth {
    @TableId(type = IdType.AUTO)
    private Long id ;
    private String name ;
    private String permission;
    private String description;
    // 0 可用，1禁用
    private Integer status;
    private String createTime;
    private String updateTime;
}
