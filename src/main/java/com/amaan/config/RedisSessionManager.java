package com.amaan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 佛祖保佑，永无BUG
 * session管理配置类
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-10-29 9:46
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionManager {
}
