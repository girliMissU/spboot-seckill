package com.amaan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-03 20:12
 */
//@Component
public class UserLogConsumer {

//    /**
//     * 启动spring容器时注册主题
//     */
//    @Bean
//    public void configTopic(){
//        kafkaTemplate.send("topic_hello", "topic_hello registered ！");
//    }

    @KafkaListener(topics = {"user-log"})
    public void consumer(ConsumerRecord<?,?> consumerRecord){
        //判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        System.out.println(">>>>>>>>>> record =" + kafkaMessage);
        //非空则isPresent=true
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            System.err.println("消费消息:"+message);
        }
    }
}
