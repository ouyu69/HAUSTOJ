package com.example.haustoj.pojo.dto;

import lombok.Data;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-30 14:54
 **/
@Data
public class RegisterDto {
    String username;
    String email;
    String password;
    String registerCode;
}
