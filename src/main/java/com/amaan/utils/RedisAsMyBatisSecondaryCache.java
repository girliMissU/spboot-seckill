package com.amaan.utils;

import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 佛祖保佑，永无BUG
 * xml中<cache/>是启用Mybatis二级缓存，启用redis代替其本地缓存，<cache type="xxx.RedisCache"/>
 * 这是使用redis代替Mybatis二级缓存，是dao层缓存
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-10-27 13:52
 */
public class RedisAsMyBatisSecondaryCache implements Cache {

    /**
     * 放入缓存的mapper的namespace
     */
    private final String id;
    public RedisAsMyBatisSecondaryCache(String id) {
        this.id = id;
    }
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object o, Object o1) {

    }

    @Override
    public Object getObject(Object o) {
        return null;
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {
        //清缓存
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
