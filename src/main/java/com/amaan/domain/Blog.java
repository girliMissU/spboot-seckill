package com.amaan.domain;

/**
 * 佛祖保佑，永无BUG
 * 博客实体，用于排行榜
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 19:32
 */
public class Blog {
    Integer blogId;
    String title;
    String context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + blogId +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
