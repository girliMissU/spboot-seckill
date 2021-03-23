package com.amaan.utils;

import com.alibaba.fastjson.JSON;
import com.amaan.annotation.ReqDedup;
import com.amaan.service.impl.ReqDedupService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 佛祖保佑，永无BUG
 * AOP类，切住所有加了@Redup注解的方法，并去执行相应逻辑
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-03-07 17:02
 */
@Aspect
@Component
public class ReqDedupHandler {

    private Logger log = LoggerFactory.getLogger(ReqDedupHandler.class);

    @Autowired
    ReqDedupService reqDedupService;

    @Pointcut("@annotation(com.amaan.annotation.ReqDedup)")
    public void reqDedupHandler(){

    }

    @Before("reqDedupHandler()")
    public void doBefore(){

    }

    @Around("reqDedupHandler()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (handleRequest(joinPoint)){
            throw new Exception("重复请求...");
        }
        return joinPoint.proceed();
    }

    private boolean handleRequest(ProceedingJoinPoint joinPoint) {
        Boolean result = false;
        log.info("========判断是否是重复请求=======");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取自定义注解值
        ReqDedup autoIdempotent = methodSignature.getMethod().getDeclaredAnnotation(ReqDedup.class);
        long expireTime = autoIdempotent.expireTime();
        // 获取参数名称
        String methodsName = methodSignature.getMethod().getName();
        String[] params = methodSignature.getParameterNames();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        Map<String, Object> reqMaps = new HashMap<>();
        for(int i=0; i<params.length; i++){
            reqMaps.put(params[i], args[i]);
        }
        String excludeKey = "Time";
        String reqJSON = ReqDedupHelper.dedupParamMD5(JSON.toJSONString(reqMaps),excludeKey);
        result = reqDedupService.checkRequest(methodsName, reqJSON, expireTime);
        return result;
    }
}
