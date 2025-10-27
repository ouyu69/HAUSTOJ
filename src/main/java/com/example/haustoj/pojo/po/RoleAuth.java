package com.example.haustoj.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ouyu69
 * @Date: 2025-10-27 18:39
 * @Description:
 */
@Data
@TableName("role_auth")
public class RoleAuth {
    private Long id;
    private Long roleId;
    private Long authId;
    private Date createTime;
    private Date updateTime;
}
