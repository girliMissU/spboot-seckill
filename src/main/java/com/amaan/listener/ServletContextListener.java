package com.amaan.listener;

import com.amaan.domain.User;
import com.amaan.service.IUserService;
import com.amaan.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 佛祖保佑，永无BUG
 * 使用ApplicationListener来初始化一些数据到application域中的监听器
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 19:38
 */
//@Component
public class ServletContextListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 项目中可以根据实际业务场景，也可以获取其他的bean，然后再调用自己的业务代码获取相应的数据，最后存储到 application 域中
     * 这样前端在请求相应数据的时候，就可以直接从 application 域中获取信息，减少数据库的压力。
     * 相当于缓存，不过是本地缓存，被redis替代
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //先获取到application上下文
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        //获取对应的service
        IUserService userService = applicationContext.getBean(UserServiceImpl.class);
        User user = userService.findById(1);
        //获取application域对象，将查到的信息放到application域中
        ServletContext application = applicationContext.getBean(ServletContext.class);
        application.setAttribute("user",user);
    }
}
