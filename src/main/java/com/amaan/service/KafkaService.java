package com.amaan.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;


/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-28 22:14
 */
public interface KafkaService {

    void consumer(ConsumerRecord<?,?> consumerRecord);
}
