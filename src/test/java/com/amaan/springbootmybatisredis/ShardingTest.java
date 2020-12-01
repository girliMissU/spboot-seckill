package com.amaan.springbootmybatisredis;

import com.amaan.SpringbootMybatisRedisApplication;
import com.amaan.dao.RunDataDao;
import com.amaan.domain.RunData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-30 19:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisRedisApplication.class)
public class ShardingTest {

    @Autowired
    RunDataDao runDataDao;

    @Test
    public void testSharding(){
        RunData data = new RunData();
        data.setRotate(3679);
        data.setCurrent(10);
        data.setGarageId("1");
//        data.setDate(new Date());
        runDataDao.addData(data);
    }
}
