package com.amaan.sbmr;

import com.amaan.SpringbootMybatisRedisApplication;
import com.amaan.kafka.consumer.DiagnoseResultConsumer;
import com.amaan.kafka.consumer.UserLogConsumer;
import com.amaan.kafka.producer.RunningDataProducer;
import com.amaan.kafka.producer.UserLogProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-28 15:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisRedisApplication.class)
public class KafkaTest {
    @Autowired
    UserLogProducer producer;
    @Autowired
    UserLogConsumer consumer;
    //method must be public
    @Autowired
    RunningDataProducer runningDataProducer;
    @Autowired
    DiagnoseResultConsumer diagnoseResultConsumer;

    @Test
    public void testProduce(){
        producer.sendLog("6");
//        consumer.consumer(new ConsumerRecord<>("user-log", 0, 0, "5", "2"));
    }

    @Test
    public void testDiagnose(){

    }
}
