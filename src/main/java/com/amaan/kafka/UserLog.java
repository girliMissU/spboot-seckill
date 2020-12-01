package com.amaan.kafka;

import java.io.Serializable;

/**
 * 佛祖保佑，永无BUG
 * 用户发送的日志数据
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-03 20:07
 */
public class UserLog implements Serializable {
    private String username;
    private String userId;
    private String state;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                "username='" + username + '\'' +
                ", userid='" + userId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
