package com.amaan.service.impl;

import com.amaan.kafka.consumer.UserLogConsumer;
import com.amaan.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-28 22:15
 */
@Service
public class KafkaServiceImpl implements KafkaService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    @KafkaListener(topics = {"topic_test"})
//    @KafkaHandler
    public void consumer(ConsumerRecord<?, ?> consumerRecord) {
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
}
