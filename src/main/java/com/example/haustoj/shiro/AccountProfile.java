package com.example.haustoj.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author ouyu
 * @Date 2025-10-26
 * @Description
 **/
@Data
public class AccountProfile implements Serializable {
    private String uuid;
    private String username;
    private String email ;
    private Integer status;
    public String getId() { //shiro登录用户实体默认主键获取方法要为getId
        return uuid;
    }
}
