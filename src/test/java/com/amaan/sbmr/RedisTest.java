package com.amaan.sbmr;

import com.amaan.SpringbootMybatisRedisApplication;
import com.amaan.dao.BlogDao;
import com.amaan.domain.BlogRank;
import com.amaan.domain.User;
import com.amaan.dao.UserMapper;
import com.amaan.service.IBlogRankService;
import com.amaan.service.IUserService;
import com.amaan.service.SeckillService;
import com.amaan.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 佛祖保佑，永无BUG
 * redis做缓存数据库，service层需要数据，不直接去dao，先从redis缓存中获得，如果命中直接返回；
 * 如果redis缓存中没有，调用dao层查询数据，将查询到的数据返回，并将查询到的数据放到redis当中一份。
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-10-14 13:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisRedisApplication.class)
public class RedisTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IBlogRankService blogRankService;
    @Autowired
    private BlogDao blogDao;

    @Test
    public void testMybatis(){
        List<User> users = userMapper.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test() throws JsonProcessingException {
        String name = redisTemplate.boundValueOps("name").get();
        if (name==null){
            List<User> users = userMapper.findAll();
            System.out.println("从MySQL中获得的值："+users);
            //将users转换成json字符串
            ObjectMapper om = new ObjectMapper();
            name = om.writeValueAsString(users);
            redisTemplate.boundValueOps("name").set(name);
        }else{
            //反序列化
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(name, User.class);
            System.out.println("从redis中获得的值："+user);
        }
    }

    @Test
    public void testWithUtils() throws JsonProcessingException {
        Object name = redisUtils.get("name");
        if (name==null){
            List<User> users = userMapper.findAll();
            System.out.println("从MySQL中获得的值："+users);
            //将users转换成json字符串
            ObjectMapper om = new ObjectMapper();
            name = om.writeValueAsString(users);
            redisUtils.set("name",name);
        }else{
            System.out.println("从redis中获得的值："+name);
        }
    }

    @Test
    public void testFindById() {
        System.out.println(userService.findById(1));
    }
    @Test
    public void testGetById() {
//        System.out.println(seckillService.getById(1000));
        System.out.println(seckillService.getByIdWithBloom(1000));
    }

    @Test
    public void testUpdate() {
        userService.updateById(2,"天刀无双");
    }

    @Test
    public void testBlogRank(){
//        blogRankService.insertBlogRank(6,4);
//        blogRankService.updateRank(7,7);
//        for (Object o : blogRankService.getRankByBound(10)) {
//            System.out.println(o);
//        }
        List<BlogRank> ranks = redisUtils.reverseRangeByLexWithScores("BLOG_RANK::0", 0, 10);
        for (BlogRank rank : ranks) {
            System.out.println((rank.toString()));
        }
        blogDao.updateBlogRank(ranks);
    }
}
