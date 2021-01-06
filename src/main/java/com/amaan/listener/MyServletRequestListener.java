package com.amaan.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * 佛祖保佑，永无BUG
 * 获取一些访问信息
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 21:19
 */
@Component
public class MyServletRequestListener implements ServletRequestListener {
    private static final Logger logger = LoggerFactory.getLogger(MyServletRequestListener.class);

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
//        logger.info("session id为：{}", request.getRequestedSessionId());
        logger.info("request url为：{}", request.getRequestURL());
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        logger.info("request end");
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        logger.info("request域中保存的username值为：{}", request.getSession().getAttribute("username"));
    }
}
