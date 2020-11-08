package com.amaan.utils;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 佛祖保佑，永无BUG
 *  Token用JWT，就是个字符串，发给客户端去存着，每次访问带着，服务端不用存就可以解析出来
 *  安全性不好，没有加密
 *  性能不好，Token字符串太长
 *  一次性，向更换必须再发新的Token
 *  优点是无状态
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-02 15:15
 */
public class TokenUtil {

    public static String getTokenUserId() {
        return null;
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }
}
