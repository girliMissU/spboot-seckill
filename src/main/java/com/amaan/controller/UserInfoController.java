package com.amaan.controller;

import com.amaan.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-02 16:25
 */
@RestController
@Api("Swagger2 登录接口文档")
public class UserInfoController {
    /**
     * 模拟数据库
     */
    private static final Map<String,String> DB = new ConcurrentHashMap<>();
    {
        DB.put("admin","123456");
    }

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public String login(@RequestParam("username") String username,@RequestParam("pwd") String password, HttpServletRequest request){
        //查数据库核对用户名和登陆密码
        if (!DB.containsKey(username)){
            return JsonUtil.getJSONString(-1,"用户不存在");
        }
        if (!password.equals(DB.get(username))){
            return JsonUtil.getJSONString(-2,"密码错误");
        }
        //如果成功，将session更新
        request.getSession().setAttribute("username",username);
        return JsonUtil.getJSONString(0,"登陆成功");
    }

}
