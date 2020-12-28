package com.amaan.domain;

/**
 * 佛祖保佑，永无BUG
 * 当月最热博客排行榜
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 17:28
 */
public class BlogRank {
    /**
     * 主键ID
     */
    Integer id;
    /**
     * 博客对象ID
     * member
     */
    Integer blogID;
    /**
     * 集合 key
     */
    String key;
    /**
     * 分数
     */
    Double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlogID() {
        return blogID;
    }

    public void setBlogID(Integer blogID) {
        this.blogID = blogID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "BlogRank{" +
                "id=" + id +
                ", blogID=" + blogID +
                ", key='" + key + '\'' +
                ", score=" + score +
                '}';
    }
}
