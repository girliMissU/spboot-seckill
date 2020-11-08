package com.amaan.controller;

import com.amaan.utils.JsonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-02 16:25
 */
@RestController
public class UserInfoController {

    @GetMapping("/login")
    public String login(@RequestParam("username") String username, HttpServletRequest request){
        //查数据库核对用户名和登陆密码
        //如果成功，将session更新
        request.getSession().setAttribute("username",username);
        return JsonUtil.getJSONString(0,"登陆成功");
    }

}
