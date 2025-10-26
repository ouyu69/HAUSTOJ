package com.example.haustoj.shiro;

import com.example.haustoj.utils.RedisUtils;
import lombok.Data;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @FileName ShiroCacheManager
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
@Data
public class ShiroCacheManager implements CacheManager {
    private Long cacheLiveTime;
    private String cacheKeyPrefix;
    private RedisUtils redisUtils;
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCache<K, V>(cacheLiveTime, cacheKeyPrefix, redisUtils);
    }
}
