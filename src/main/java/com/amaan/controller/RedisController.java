package com.amaan.controller;

import com.amaan.domain.User;
import com.amaan.service.IUserService;
import com.amaan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 佛祖保佑，永无BUG
 * 测试Redis
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-09-24 21:21
 */
@RestController
public class RedisController {

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("setAndGet")
    public String test(String k, String v){
        redisUtils.set(k,v);
        return (String) redisUtils.get(k);
    }

    @Autowired
    private IUserService userService;

    @RequestMapping("/adduser")
    public int addUser(@RequestParam("name")String name,@RequestParam("age")String age){
        return userService.addUser(name, age);
    }
    @RequestMapping("/findUser")
    public User findUser(@RequestParam("id") Integer id){
        return userService.findById(id);
    }
    @RequestMapping("/updataById")
    public String updataById(@RequestParam("id") Integer id,@RequestParam("name") String name){
        try {
            userService.updataById(id, name);
        } catch (Exception e) {
            return"error";
        }
        return"success";
    }

    @RequestMapping("/deleteById")
    public String deleteById(@RequestParam("id") Integer id){
        try {
            userService.deleteById(id);
        } catch (Exception e) {
            return"error";
        }
        return"success";
    }

    /**
     * 测试分布式session管理
     * 无法进行计数，size只能到2，因为每次改的是JVM中的list的内容，但并没有更改redis中session中的list，也没有更新到redis中，所以redis中list.size永远是1
     */
    @RequestMapping("/test/session")
    public void testRSM(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> list = (List<String>) request.getSession().getAttribute("list");
        if (list==null){
            list = new ArrayList<>();
            request.getSession().setAttribute("list",list);
        }
        list.add("xxxxxxx");
        response.getWriter().println("size:"+list.size());
        response.getWriter().println("sessionid:"+request.getSession().getId());
    }
    /**
     * 每次session变化要进行redis同步
    *  request.getSession().isNew()
    *  request.getSession().setMaxInactiveInterval(1000)
     *  getId()
     *  getAttribute("list")
     *  getCreationTime()
     *  getLastAccessedTime()
     *  getMaxInactiveInterval()
     */
    @RequestMapping("/session")
    public void testRSM2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> list = (List<String>) request.getSession().getAttribute("list");
        if (list==null){
            list = new ArrayList<>();
        }

        list.add("1101");
        //同步redis
        request.getSession().setAttribute("list",list);
        response.getWriter().println("size:"+list.size());
        response.getWriter().println("sessionid:"+request.getSession().getId());
    }
    /**
     * 退出session
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request){
        //redis会自动注销
        request.getSession().invalidate();
    }
}
