package com.amaan.service.impl;

import com.amaan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-03-07 17:11
 */
@Service
public class ReqDedupService {

    @Autowired
    RedisUtils redisUtils;
    private String TOKEN_NAME = "request_token";

    public Boolean checkRequest(String methodsName, String reqJSON, long expireTime) {
        String expireAt = String.valueOf(System.currentTimeMillis()+expireTime);
        return redisUtils.setNXWithExpire(methodsName + reqJSON, expireAt, expireTime, TimeUnit.MILLISECONDS);
    }
}
