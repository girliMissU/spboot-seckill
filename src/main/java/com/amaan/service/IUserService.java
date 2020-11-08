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

    public User findById(Integer id);

    public int addUser(String name,String age);

    public void updataById(Integer id,String name);

    public void deleteById(Integer id);
}
