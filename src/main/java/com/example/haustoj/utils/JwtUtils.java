package com.example.haustoj.utils;

import com.example.haustoj.shiro.ShiroConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @FileName JwtUtils
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Slf4j(topic = "haustoj-jwt")
@Data
@Component
public class JwtUtils {
    private String secret ;
    private long expire ;
    private String header ;
    private long checkRefreshExpire;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 生成jwt token
     */
    public String generateToken(String userId) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        redisUtils.set(ShiroConstant.SHIRO_TOKEN_KEY + userId, token, expire);
        redisUtils.set(ShiroConstant.SHIRO_TOKEN_REFRESH + userId, "1", checkRefreshExpire);
        return token;
    }
    
    /** 
     * @param token
     * @return Claims
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }
    /**
     * 清除redis缓存中的访问token和刷新token
     */
    public void cleanToken(String uid) {
        redisUtils.del(ShiroConstant.SHIRO_TOKEN_KEY + uid, ShiroConstant.SHIRO_TOKEN_REFRESH + uid);
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
    public Boolean hasToken(Long uid) {
        return redisUtils.hasKey(ShiroConstant.SHIRO_TOKEN_KEY + uid);
    }

}
