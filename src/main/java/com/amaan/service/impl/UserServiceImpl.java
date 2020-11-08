package com.amaan.service.impl;

import com.amaan.domain.User;
import com.amaan.dao.UserMapper;
import com.amaan.service.IUserService;
import com.amaan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 佛祖保佑，永无BUG
 * @Cacheable将查询结果缓存到redis中，value指定使用缓存的名称，（key="#p0"）指定传入的第一个参数作为redis的key。
 * @CachePut，指定key，将更新的结果同步到redis中
 * @CacheEvict，指定key，删除缓存数据，allEntries=true,方法调用后将立即清除缓存
 * 默认是string类型的key-value,且查询出来的string类型的value反序列化要求所有字段的toString中都有引号包起来
 * 双写数据不一致问题：先写数据库，删缓存，另外可加延时双删，失败重试
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-09-24 21:52
 */
@Service("userService")
//@CacheConfig(cacheNames = "users", cacheManager = "redisCacheManager")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

    private static final String USER_KEY_SET="USER_KEY_SET";
    private static final String USER_PREFIX = "users::USER_";

//    @Override
//    @Cacheable(key = "'USER_'+#id")//redis生成的key为cacheNames::key
//    public User findById(Integer id){
//        return userMapper.findById(id);
//    }
    @Override
    public User findById(Integer id){
        User user;
        String userKey = USER_PREFIX+id;
        //先去缓存里找
        if (redisUtils.exists(userKey)){
                System.out.println("在缓存里找到了！");
                user = new User();
                user.setId(id);
                user.setName((String) redisUtils.hmGet(userKey, "name"));
                user.setAge((String) redisUtils.hmGet(userKey, "age"));
        }else {
        //缓存里没有去dao找
            user = userMapper.findById(id);
            if (user == null) {
                return null;
            }
            //找到后放入缓存一份
            redisUtils.hmSet(userKey, "id", user.getId() + "");
            redisUtils.hmSet(userKey, "name", user.getName());
            redisUtils.hmSet(userKey, "age", user.getAge());
            System.out.println("放入缓存了一份！");
        }
        return user;
    }

    @Override
    public int addUser(String name,String age){
        //写入数据库，让下次查询去放缓存
        return userMapper.addUser(name,age);
    }

    @Override
    //@CachePut(key = "#p0")
    public void updataById(Integer id,String name){
        //先更新数据库
        userMapper.updataById(id,name);
        String userKey = USER_PREFIX+id;
//        //先去缓存里找
//        if (redisUtils.exists(userKey)){
//            System.out.println("在缓存里找到了！");
//            redisUtils.hmSet(userKey,"name",name);
//        }
        redisUtils.remove(userKey);
    }
    @Override
    //如果指定为 true，则方法调用后将立即清空所有缓存
    //@CacheEvict(key ="#p0",allEntries=true)
    public void deleteById(Integer id){
        //先更新数据库
        userMapper.deleteById(id);
        //先去缓存里找
        redisUtils.remove(USER_PREFIX+id);
    }

}
