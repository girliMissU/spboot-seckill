package com.amaan.config;

import com.amaan.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 佛祖保佑，永无BUG
 * 弃用的WebMvcConfigurerAdapter也不会拦截静态资源，但继承其替代者WebMvcConfigurationSupport会
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 19:31
 */
@Configuration
public class SessionInterceptorConfig implements WebMvcConfigurer {
    //实现WebMvcConfigurer不会导致静态资源被拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //只写"/"的话，/seckill/list不会被拦截，那可不行啊！
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**");
    }
}
