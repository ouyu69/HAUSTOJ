package com.example.haustoj.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @FileName AccountProfile
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Data
public class AccountProfile implements Serializable {
    private Long id;
    private String username;
    private String email ;
    private Integer status;
}
