package com.example.haustoj.shiro;

import com.example.haustoj.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;


/**
 * @FileName ShiroCache
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/
public class ShiroCache<K,V> implements Cache<K,V> {
    private Long cacheLiveTime;
    private String cacheKeyPrefix;
    private RedisUtils redisUtils;
    private ObjectMapper objectMapper;
    public ShiroCache(Long cacheLiveTime, String cacheKeyPrefix, RedisUtils redisUtils,ObjectMapper objectMapper) {
        this.cacheLiveTime = cacheLiveTime;
        this.cacheKeyPrefix = cacheKeyPrefix;
        this.redisUtils = redisUtils;
        this.objectMapper = objectMapper;
    }

    /**
     * 缓存的key名称获取为shiro:cache:account
     *
     * @param key
     */
    private String getKey(K key) {
        String userId;
        if (key instanceof PrincipalCollection) {
            AccountProfile accountProfile = (AccountProfile) ((PrincipalCollection) key).getPrimaryPrincipal();
            userId = accountProfile.getId();
        } else {
            userId = key.toString();
        }
        return this.cacheKeyPrefix + userId;
    }

    /** 
     * @param k
     * @return V
     * @throws CacheException
     */
    @Override
    public V get(K k) throws CacheException {
        try {
            V v = (V) fromJson((String) this.redisUtils.get(getKey(k)), SimpleAuthorizationInfo.class);
            return v;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public V put(K k, V v) throws CacheException {
        try {
            String json = (String) this.toJson(v);
            this.redisUtils.set(getKey(k), json, cacheLiveTime);
            return v;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public V remove(K k) throws CacheException {
        if(!this.redisUtils.hasKey(getKey(k))){
            return null;
        }
        redisUtils.del(getKey(k));
        return null;
    }

    @Override
    public void clear() throws CacheException {
        Set<String> keys = this.redisUtils.keys(this.cacheKeyPrefix + "*");
        if (null != keys && keys.size() > 0) {
            this.redisUtils.del(keys);
        }
    }

    @Override
    public int size() {
        Set<String> keys = this.redisUtils.keys(this.cacheKeyPrefix + "*");
        return keys.size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) this.redisUtils.keys(this.cacheKeyPrefix + "*");
    }

    @Override
    public Collection<V> values() {
        Set<String> keys = this.redisUtils.keys(this.cacheKeyPrefix + "*");
        if(CollectionUtils.isEmpty( keys)){
            return Collections.emptySet();
        }else{
            List<Object> values = new ArrayList<>(keys.size());
            Iterator iterator = keys.iterator();
            try {
                while(iterator.hasNext()){
                    String json = (String) this.redisUtils.get((String) iterator.next());
                    V v = (V) fromJson(json,  SimpleAuthorizationInfo.class);
                    if(v != null){
                        values.add(v);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (Collection<V>) values;
        }
    }
    private String toJson(Object obj) throws Exception {
        if (obj instanceof SimpleAuthorizationInfo) {
            SimpleAuthorizationInfo info = (SimpleAuthorizationInfo) obj;
            Map<String, Object> map = new HashMap<>();
            map.put("roles", info.getRoles());
            map.put("stringPermissions", info.getStringPermissions());
            map.put("objectPermissions", info.getObjectPermissions());
            return objectMapper.writeValueAsString(map);
        }
        return objectMapper.writeValueAsString(obj);
    }
    private <T> T fromJson(String json, Class<T> clazz) throws Exception {
        if (json == null || json.isEmpty()) {
            return null; // 或者抛出业务友好的异常
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }
}
