package com.amaan.utils;

import com.amaan.config.BloomFilterHelper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-09-24 21:11
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    private static double size = Math.pow(2, 32);

    //第一次加载的时候将数据加载到redis中
    public void saveDataToRedis(String name) {
        double index = Math.abs(name.hashCode() % size);
        long indexLong = new Double(index).longValue();
        boolean availableUsers = setBit("availableUsers", indexLong, true);
    }

    //第一次加载的时候将数据加载到redis中
    public boolean getDataToRedis(String name) {
        double index = Math.abs(name.hashCode() % size);
        long indexLong = new Double(index).longValue();
        return getBit("availableUsers", indexLong);
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("bloom add key : " + key + " " + "value : " + i);
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("bloom search key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 写入缓存 * *
     * @param key *
     * @param offset 位 8Bit=1Byte *
     * @return
     */
    public boolean setBit(String key, long offset, boolean isShow) {
        boolean result = false;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            operations.setBit(key, offset, isShow);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param offset
     * @return
     */
    public boolean getBit(String key, long offset) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            result = operations.getBit(key, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的value * * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 删除对应的value * * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的key
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 哈希 添加 * *
     * @param key *
     * @param hashKey *
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据 * *
     * @param key *
     * @param hashKey *
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加 * * @param k * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取 * * @param k * @param l * @param l1 * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加 * * @param key * @param value
     */
    public void sAdd(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 获取集合中全部元素
     * @ param key 集合的key
     * @return 全部元素的set视图
     */
    public Set<Object> getMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 从集合中删除元素
     * @param key
     * @param value
     */
    public void sRem(String key, Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.remove(key,value);
    }

    /**
     * 有序集合添加 * * @param key * @param value * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取 * * @param key * @param scoure * @param scoure1 * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        redisTemplate.opsForValue();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 有序集合获取排名 * * @param key 集合名称 * @param value 值
     */
    public Long zRank(String key, Object value) {
        ZSetOperations zset = redisTemplate.opsForZSet();
        return zset.rank(key, value);
    }

    /**
     * 有序集合获取排名 * * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
        ZSetOperations zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.rangeWithScores(key, start, end);
        return ret;
    }

    /**
     * 有序集合添加 * * @param key * @param value
     */
    public Double zSetScore(String key, Object value) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, value);
    }

    /**
     * 有序集合添加分数 * * @param key * @param value * @param scoure
     */
    public void incrementScore(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, value, scoure);
    }

    /**
     * 有序集合获取排名 * * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.reverseRangeByScoreWithScores(key, start, end);
        return ret;
    }

    /**
     * 有序集合获取排名 * * @param key
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.reverseRangeWithScores(key, start, end);
        return ret;
    }
}
