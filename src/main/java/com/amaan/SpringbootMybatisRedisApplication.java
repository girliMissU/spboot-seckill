package com.amaan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author John
 *
 * (exclude={DataSourceAutoConfiguration.class})
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("com.amaan.dao")
@EnableTransactionManagement
public class SpringbootMybatisRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisRedisApplication.class, args);
    }

}
