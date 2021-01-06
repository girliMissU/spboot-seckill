package com.amaan.service;

import com.amaan.domain.Blog;

import java.util.List;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 19:35
 */
public interface IBlogRankService {
    /**
     * 生成/更新排行榜
     * @param blogID 博客ID
     * @param score 加分/减分
     */
    void insertBlogRank(Integer blogID, Integer score);

    /**
     * 获取排行榜前N
     * @param bound 默认10
     * @return 排行前N的博客
     */
    List<Blog> getRankByBound(Integer bound);

    List<Blog> getBlog(List<Integer> blogIDs);

    void updateRank(Integer blogID, Integer delta);

    void persistRank();
}
