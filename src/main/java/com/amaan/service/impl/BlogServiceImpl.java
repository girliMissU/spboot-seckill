package com.amaan.service.impl;

import com.amaan.dao.BlogDao;
import com.amaan.domain.Blog;
import com.amaan.service.IBlogRankService;
import com.amaan.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 19:42
 */
@Service
public class BlogServiceImpl implements IBlogRankService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BlogDao blogDao;

    private static final String BLOG_RANK_KEY = "BLOG_RANK::0";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 加入到排行榜中
     * @param blogID 博客ID
     * @param score  初始分数
     */
    @Override
    public void insertBlogRank(Integer blogID, Integer score) {
        redisUtils.zAdd(BLOG_RANK_KEY,blogID,score);
    }

    /**
     * 获取排行榜前N
     *
     * @param bound 默认10
     * @return 排行前N的博客
     */
    @Override
    public List<Blog> getRankByBound(Integer bound) {
        List<Object> objects = redisUtils.reverseRangeByLex(BLOG_RANK_KEY, 0, bound);
        List<Integer> blogIDs = new ArrayList<>();
        for (Object o : objects) {
            System.out.println(o);
            blogIDs.add((Integer) o);
        }
        return blogDao.getBlogs(blogIDs);
    }

    @Override
    public List<Blog> getBlog(List<Integer> blogIDs) {
        return blogDao.getBlogs(blogIDs);
    }

    @Override
    public void updateRank(Integer blogID, Integer delta) {
        if (redisUtils.zSetScore(BLOG_RANK_KEY,blogID)!=null){
            redisUtils.incrementScore(BLOG_RANK_KEY,blogID,delta);
        }else{
            redisUtils.zAdd(BLOG_RANK_KEY,blogID,delta);
        }
    }
}
