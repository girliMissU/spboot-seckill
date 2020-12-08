package com.amaan.listener;

import com.amaan.domain.User;
import org.springframework.context.ApplicationEvent;

/**
 * 佛祖保佑，永无BUG
 * 自定义事件
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 21:30
 */
public class CustomizedEvent extends ApplicationEvent {

    private User user;

    public CustomizedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CustomizedEvent{" +
                "user=" + user +
                ", source=" + source +
                '}';
    }
}
