package com.amaan.dao;

import com.amaan.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * 佛祖保佑，永无BUG
 * 用@Mapper和@MapperScan()不要同时用
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-09-24 21:47
 */
//@Mapper
public interface UserMapper {

    @Insert("insert into user(name,age) values(#{name},#{age})")
    int addUser(@Param("name")String name,@Param("age")String age);

    @Select("select * from user where id =#{id}")
    User findById(@Param("id") Integer id);

    @Update("update user set name=#{name} where id=#{id}")
    void updateById(@Param("id")Integer id,@Param("name")String name);

    @Delete("delete from user where id=#{id}")
    void deleteById(@Param("id")Integer id);

    @Select("select * from user")
    List<User> findAll();
}
