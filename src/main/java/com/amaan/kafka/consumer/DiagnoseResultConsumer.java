package com.amaan.kafka.consumer;

import com.amaan.kafka.producer.RunningDataProducer;
import com.amaan.utils.RedisUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-03-02 10:42
 */
@Component
public class DiagnoseResultConsumer {

    private static final String DIAGNOSE_RESULT = "FaultDiagnoseResult";

    @Autowired
    RedisUtils redisUtils;

    @KafkaListener(topics = {RunningDataProducer.DIAGNOSE_TOPIC})
//    @KafkaListener(topics = {DIAGNOSE_RESULT})
    @KafkaHandler
    public void consumer(ConsumerRecord<?,?> consumerRecord, Acknowledgment acknowledgment){
        //判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        //非空则isPresent=true
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            redisUtils.lPush(RunningDataProducer.DIAGNOSE_KEY,message);
        }
        acknowledgment.acknowledge();
    }
}
