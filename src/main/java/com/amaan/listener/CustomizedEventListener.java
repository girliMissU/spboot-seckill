package com.amaan.listener;

import com.amaan.domain.User;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 佛祖保佑，永无BUG
 * 使用方法：
 *    service中
 *      @Autowired
 *      private ApplicationContext applicationContext;
 *     方法中发布事件{
 *         CustomizedEvent event = new CustomizedEvent(this,user);
 *         applicationContext.publishEvent(event);
 *     }
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-08 21:32
 */
//@Component
public class CustomizedEventListener implements ApplicationListener<CustomizedEvent> {
    @Override
    public void onApplicationEvent(CustomizedEvent myEvent) {
        // 把事件中的信息获取到
        User user = myEvent.getUser();
        // 处理事件，实际项目中可以通知别的微服务或者处理其他逻辑等等
        System.out.println("用户名：" + user.getName());
        System.out.println("年龄：" + user.getAge());
    }
}
