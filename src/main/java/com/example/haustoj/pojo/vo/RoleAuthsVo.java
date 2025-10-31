package com.example.haustoj.pojo.vo;

import com.example.haustoj.pojo.po.Auth;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-29 16:24
 **/
@Data
public class RoleAuthsVo {
    private Long id;
    private String name;
    private String description;
    private Integer status;
    private String createTime;
    private String updateTime;
    private List<Auth> authList;

}
