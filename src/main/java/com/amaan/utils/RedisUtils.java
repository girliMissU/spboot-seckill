package com.amaan.utils;

import com.alibaba.fastjson.JSON;
import com.amaan.config.BloomFilterHelper;
import com.amaan.domain.BlogRank;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
     * set if not exist with expire
     * 原子性
     */
    public boolean setNXWithExpire(String key, String value, long timeout, TimeUnit timeUnit){
        BoundValueOperations ops = redisTemplate.boundValueOps(key);
        return ops.setIfAbsent(value,timeout,timeUnit);
    }

    /**
     * 写入缓存
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
     * 非原子性
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量删除对应的key
     * @param keys
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
     * 有序集合添加
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, score);
    }

    /**
     * 获取排行范围内的对象
     */
    public List<Object> reverseRangeByLex(String key, int min, int max){
        List<Object> objects = (List<Object>) redisTemplate.execute((RedisCallback<List<Object>>) redisConnection -> {
            Set<byte[]> bytes1 = redisConnection.zRevRange(key.getBytes(), min, max);
            List<Object> os = new ArrayList<>();
            if (bytes1.size() == 0) {
                return os;
            }
            for (byte[] bytes : bytes1) {
//                    System.out.println(Arrays.toString(bytes));
                String s = new String(bytes);
                Object o = JSON.parse(s);
//                System.out.println(o);
                os.add(o);
            }
            return os;
        });
        return objects;
    }
    public List<BlogRank> reverseRangeByLexWithScores(String key, int min, int max){
        List<BlogRank> ranks = (List<BlogRank>) redisTemplate.execute((RedisCallback<List<BlogRank>>) redisConnection -> {
            Set<RedisZSetCommands.Tuple> tuples = redisConnection.zRevRangeWithScores(key.getBytes(), min, max);
            List<BlogRank> os = new ArrayList<>();
            if (tuples.size() == 0) {
                return os;
            }
            int rankID=0;
            for (RedisZSetCommands.Tuple tuple : tuples) {
                BlogRank blogRank = new BlogRank();
                blogRank.setId(++rankID);
                blogRank.setKey(key);
                blogRank.setBlogID(Integer.parseInt(new String(tuple.getValue())));
                blogRank.setScore(tuple.getScore());
//                System.out.println(o);
                os.add(blogRank);
            }
            return os;
        });
        return ranks;
    }

    /**
     * 有序集合获取分数在范围内的对象
     */
    public Set<Object> rangeByScore(String key, double score, double score1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//        redisTemplate.opsForValue();
        return zset.rangeByScore(key, score, score1);
    }

    /**
     * 有序集合获取排名
     * @param key 集合名称
     * @param o 要获取排名的对象
     */
    public Long zRank(String key, Object o) {
        ZSetOperations zset = redisTemplate.opsForZSet();
        return zset.rank(key, o);
    }

    /**
     * 根据分数
     * 有序集合获取排名
     * 结果还带有分数
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScore(String key, long start, long end) {
        ZSetOperations zset = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> ret = zset.rangeWithScores(key, start, end);
        return ret;
    }

    /**
     * zset 获取分数
     */
    public Double zSetScore(String key, Object o) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.score(key, o);
    }

    /**
     * 有序集合加分
     */
    public void incrementScore(String key, Object o, double delta) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.incrementScore(key, o, delta);
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
