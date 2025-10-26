package com.example.haustoj.config;

import com.example.haustoj.shiro.*;
import com.example.haustoj.utils.RedisUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @FileName ShiroConfig
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Configuration
public class ShiroConfig {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private JwtFilter jwtFilter;

    @Value("${haust.jwt.expire:86400}")
    private long expire;

    public ShiroConfig() {
    }
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("getAccountRealm") AccountRealm accountRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
        ShiroCacheManager shiroCacheManager = new ShiroCacheManager();
        shiroCacheManager.setCacheLiveTime(expire);
        shiroCacheManager.setCacheKeyPrefix(ShiroConstant.SHIRO_AUTHORIZATION_CACHE);
        shiroCacheManager.setRedisUtils(redisUtils);
        securityManager.setCacheManager(shiroCacheManager);

        /**
         * 关闭shiro自带的session
         */
        securityManager.setSessionManager(null);
        return securityManager;
    }
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "jwt"); // 主要通过注解方式校验权限
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager, @Qualifier("shiroFilterChainDefinition") ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilterFactoryBean.setFilters(filters);
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    AccountRealm getAccountRealm() {
        return new AccountRealm();
    }

}
