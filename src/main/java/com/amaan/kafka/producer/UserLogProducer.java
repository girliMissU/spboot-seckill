package com.amaan.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.amaan.kafka.UserLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-03 20:09
 */
@Component
public class UserLogProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogProducer.class);

    @Autowired
    KafkaTemplate kafkaTemplate;

    public void sendLog(String userId){
        UserLog userLog = new UserLog();
        userLog.setUsername("kafka user");
        userLog.setUserId(userId);
        userLog.setState("0");
        LOGGER.info("发送用户日志数据:"+userLog);
        String message = JSON.toJSONString(userLog);
        /**
         * 异步发送就在这通过Future区分
         * send().get();就是同步发送
         */
        ListenableFuture future = kafkaTemplate.send("user-log", message);
        future.addCallback(new SuccessCallback() {
            @Override
            public void onSuccess(Object o) {
                LOGGER.info("发送成功，topic：" + "user-log" + "，message：" + message + "; o :" + o);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.info("发送异常："+throwable);
                //显示立即关闭producer
            }
        });
    }
}
