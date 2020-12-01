package com.amaan.controller;

import com.amaan.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-28 21:47
 */
@RestController
public class KafkaController {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @RequestMapping(path = "/test/sendKafka")
    public String testKafka(@RequestParam("msg") String message){
        StringBuffer backMsg = new StringBuffer();
        ListenableFuture future = kafkaTemplate.send("topic_test", message);
        future.addCallback(o -> {
//                LOGGER.info("发送成功，topic：" + "user-log" + "，message：" + message);
            backMsg.append(JsonUtil.getJSONString(0,"发送成功，topic：" + "topic_test" + "，message：" + message));
        }, throwable -> {
//                LOGGER.info("发送异常："+throwable);
            backMsg.append(JsonUtil.getJSONString(1,"发送异常！"));
        });
        return backMsg.toString();
    }

}
