package com.amaan.config;

import com.amaan.interceptor.SessionInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 佛祖保佑，永无BUG
 * WebMvcConfigurerAdapter被弃用，但依然可以用
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-02 15:44
 */
//@Configuration
public class SessionConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    }
}
