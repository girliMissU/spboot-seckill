package com.amaan.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.amaan.kafka.UserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-03 20:09
 */
//@Component
public class UserLogProducer {
    @Autowired
    KafkaTemplate kafkaTemplate;

    public void sendLog(String userId){
        UserLog userLog = new UserLog();
        userLog.setUsername("kafka user");
        userLog.setUserid(userId);
        userLog.setState("0");
        System.err.println("发送用户日志数据:"+userLog);
        kafkaTemplate.send("user-log", JSON.toJSONString(userLog));
    }
}
