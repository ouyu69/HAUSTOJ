package com.example.haustoj.shiro;

import com.example.haustoj.utils.RedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import java.util.*;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.cn;

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

    public ShiroCache(Long cacheLiveTime, String cacheKeyPrefix, RedisUtils redisUtils) {
        this.cacheLiveTime = cacheLiveTime;
        this.cacheKeyPrefix = cacheKeyPrefix;
        this.redisUtils = redisUtils;
    }

    /**
     * 缓存的key名称获取为shiro:cache:account
     *
     * @param key
     */
    private String getKey(K key) {
        Long userId;
        if (key instanceof PrincipalCollection) {
            AccountProfile accountProfile = (AccountProfile) ((PrincipalCollection) key).getPrimaryPrincipal();
            userId = accountProfile.getId();
        } else {
            userId = (Long) key;
        }
        return this.cacheKeyPrefix + userId;
    }

    @Override
    public V get(K k) throws CacheException {
        return (V) this.redisUtils.get(getKey(k));
    }

    @Override
    public V put(K k, V v) throws CacheException {
        this.redisUtils.set(getKey(k), v, cacheLiveTime);
        return v;
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
            while(iterator.hasNext()){
                String value = (String) this.redisUtils.get((String) iterator.next());
                if(value != null){
                    values.add(value);
                }
            }
            return (Collection<V>) values;
        }
    }
}
