package com.amaan.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 佛祖保佑，永无BUG
 * 统计用户在线数监听器
 * 会在拦截器之后运行
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 19:59
 */
@Component
public class OnlineUserListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(OnlineUserListener.class);

    /**
     * 记录在线用户数量
     */
    private AtomicInteger userCount = new AtomicInteger(0);

    /**
     * 拦截器后面运行一次，当这个请求被重定向了，重定向之后的请求不会在运行了，一个session也就是一个客户端只有一次
     * 被拦截器放过的也不会在这运行
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("session listener ====> 新用户上线了");
        userCount.addAndGet(1);
        se.getSession().getServletContext().setAttribute("user count",userCount.get());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("用户下线了");
        userCount.addAndGet(-1);
        se.getSession().getServletContext().setAttribute("count", userCount.get());
    }
}
