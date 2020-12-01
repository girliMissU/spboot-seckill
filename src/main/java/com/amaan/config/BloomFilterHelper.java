package com.amaan.config;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

/**
 * 佛祖保佑，永无BUG
 * 布隆过滤器
 * 算法过程：
 * 1.首先需要k个哈希函数，每个函数可以把key散列成为1个整数
 * 2.初始化时，需要一个长度为n比特的数组，每个比特位初始化为0
 * 3.某个key加入集合时，用k个hash函数计算出k个散列值，并把数组中对应的比特位置为1
 * 4.判断某个key是否在集合时，用k个哈希函数计算出k个哈希值，并查询数组中对应的比特位，如果所有比特位均为1，就认为是在集合中
 * 布隆认为在集合中，不一定百分百在；但布隆认为不在集合的，就一定不在，所以不是百分百过滤，存在误判
 * 由于布隆过滤器存在不可逆性，因此不建议删除功能
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-26 16:18
 */
public class BloomFilterHelper<T> {

    private int numHashFunctions;

    private int bitSize;

    private Funnel<T> funnel;

    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        // 计算bit数组长度
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        // 计算hash方法执行次数
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            // 设定最小期望长度
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
