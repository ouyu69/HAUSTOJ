package com.example.haustoj.config;

import com.example.haustoj.utils.IpUtils;
import lombok.Data;

/**
 * @Description 网页前端显示的内容等配置
 * @Author ouyu
 * @Date 2025-10-28 11:33
 **/
@Data
public class WebConfig {

    // 邮箱配置
    private String emailUsername;

    private String emailPassword;

    private String emailHost;

    private Integer emailPort;

    private Boolean emailSsl = true;

    private String emailBGImg = "https://cdn.jsdelivr.net/gh/HimitZH/CDN/images/HCODE.png";

    // 网站前端显示配置
//    private String baseUrl = "http://" + IpUtils.getServiceIp();
    private String baseUrl ;

    private String name = "Hcode Online Judge";

    private String shortName = "HAUSTOJ";

    private String description;

    private String recordName;

    private String recordUrl;

    private String projectName = "HAUSTOJ";

    private String projectUrl = "https://github.com/ouyu69/HAUSTOJ";
}
