package com.example.haustoj.utils;

/**
 * @FileName IpUtils
 * @Description
 * @Author ouyu
 * @Date 2025-10-27
 **/

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j(topic = "haustoj")
public class IpUtils {
    
    /** 
     * @param request
     * @return String
     */
    public static String getIpAddr(HttpServletRequest  request) {
        String ipAddress = null ;
        try{
            ipAddress = request.getHeader("x-forwarded-for");
            // 检查是不是反向代理
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP
                    try{
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error("用户IP获取异常------>{}",e.getMessage());
                    }
                }
            }
            if(ipAddress != null){
                if(ipAddress.contains(",")){
                    return ipAddress.split(",")[0];
                }else{
                    return ipAddress;
                }
            }else{
                return "";
            }
        } catch (Exception e) {
            log.error("用户IP获取异常------>{}",e.getMessage());
            return "";
        }
    }
}
