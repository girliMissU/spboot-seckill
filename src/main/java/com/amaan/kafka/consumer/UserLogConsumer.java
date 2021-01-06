package com.amaan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
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
@Component
public class UserLogConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogConsumer.class);

//    /**
//     * 启动spring容器时注册主题
//     */
//    @Bean
//    public void configTopic(){
//        kafkaTemplate.send("topic_hello", "topic_hello registered ！");
//    }

    /**
     * 订阅发布模式（一对多）也有两种，一是消费者主动去拉（轮询），而是队列主动去推
     * Kafka是前者，消费者会去poll轮询队列，类似select/poll模型？？？
     * 当轮循到topic有消息，会自动调用该函数
     * @param consumerRecord 已被处理过的，包装有消息的记录
     */
    @KafkaListener(topics = {"user-log"})
    @KafkaHandler
    public void consumer(ConsumerRecord<?,?> consumerRecord){
        //判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        LOGGER.info(">>>>>>>>>> record =" + kafkaMessage);
        //非空则isPresent=true
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            LOGGER.info("消费消息:"+message);
        }
    }
//    @KafkaListener(topics = {"user-log"})
//    public  void receiveDeviceData(JSONObject message){
//        System.out.println("消费消aoooo息:"+message);
//    }
}
