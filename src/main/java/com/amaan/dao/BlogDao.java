package com.amaan.dao;

import com.amaan.domain.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 21:37
 */
public interface BlogDao {
    List<Blog> getBlogs(@Param("ids") List<Integer> blogIDs);
}
