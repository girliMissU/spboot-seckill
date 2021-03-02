package com.amaan.kafka.producer;

import com.amaan.utils.JsonResult;
import com.amaan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2021-03-02 10:07
 */
@RestController
@RequestMapping("/FaultDiagnose")
public class RunningDataProducer {

    public static final String DIAGNOSE_TOPIC = "FaultDiagnoseData";
    public static final String DIAGNOSE_KEY = "SpeedDiagnose";
    private final int CASTimes = 3;

    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    RedisUtils redisUtils;

    @PostMapping("/running_data/speed")
    public void collectRunningData(@RequestParam("id")Integer id, @RequestParam("speed")Double speed){
        final boolean[] produced = {false};
            //失败自旋3此重新发送
            ListenableFuture produceData = kafkaTemplate.send(DIAGNOSE_TOPIC, DIAGNOSE_KEY, speed.toString());
            produceData.addCallback(new SuccessCallback() {
                @Override
                public void onSuccess(Object o) {
                    produced[0] = true;
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    produced[0] = false;
                }
            });
        try {
            produceData.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //写数据库
        //mapper.insert(id,speed,produced[0]);
        redisUtils.set(DIAGNOSE_KEY+System.currentTimeMillis(), speed.toString()+"::"+produced[0], (long) 60, TimeUnit.SECONDS);
        JsonResult<String> response = new JsonResult<>();
        if (produced[0]) {
            response.setCode("0");
            response.setMsg("produce successfully.");
        }else {
            response.setCode("1");
            response.setMsg("produce failed.");
        }
        System.out.println(response);
//        return response.toString();
    }

}
