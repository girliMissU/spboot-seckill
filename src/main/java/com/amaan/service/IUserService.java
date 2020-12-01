package com.amaan.service;

import com.amaan.domain.User;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-09-24 21:51
 */
public interface IUserService {

    User findById(Integer id);

    int addUser(String name, String age);

    void updateById(Integer id, String name);

    void deleteById(Integer id);
}
