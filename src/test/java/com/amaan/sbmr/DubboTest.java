package com.amaan.sbmr;

import com.amaan.SpringbootMybatisRedisApplication;
import com.amaan.dubbo.MallOrder;
import com.amaan.dubbo.service.MallOrderService;
//import org.apache.dubbo.config.annotation.DubboReference;
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
 * 2021-01-12 16:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisRedisApplication.class)
public class DubboTest {
//    @DubboReference(version = "1.0.0", interfaceName = "mallOrder service")
//    MallOrderService mallOrderService;
    @Autowired
    MallOrderService mallOrderService;

    @Test
    public void printOrder(){
        String orderId="TD106986521";
        MallOrder order = mallOrderService.getOrderById(orderId);
        System.out.println(order);
    }
}
