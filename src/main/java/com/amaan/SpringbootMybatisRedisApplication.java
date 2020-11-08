package com.amaan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author John
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("com.amaan.dao")
public class SpringbootMybatisRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisRedisApplication.class, args);
    }

}
