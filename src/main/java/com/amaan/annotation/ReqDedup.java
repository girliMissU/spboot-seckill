package com.amaan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 佛祖保佑，永无BUG
 * 接口幂等性——防刷去重注解
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-03-07 16:56
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqDedup {
    long expireTime() default 1000;
}
