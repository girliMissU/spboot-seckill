package com.amaan.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 佛祖保佑，永无BUG
 * 拦截器会对所有的请求拦截，包括重定向，因此如果不放行就会死循环
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-02 15:48
 */
public class SessionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.info("========interceptor===============");
//        logger.info(request.getRequestURI());
        //首页路径以及登录放行，此处搞不好就会死循环重定向崩掉
        if ("/index".equals(request.getRequestURI())||"/login.html".equals(request.getRequestURI())){
//            logger.warn("放行");
            return true;
        }
        //重定向
        Object object = request.getSession().getAttribute("username");
        if (object==null){
            response.sendRedirect("/login.html");
            return false;
        }
        logger.info("session interceptor =====> 用户"+object.toString()+"已登录");
        //重复请求可以在这里处理，但不是所有方法都要防止重复请求，会增加CPU资源消耗，根据URI区分，请求参数不好搞
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //执行完了该方法，但是在 DispatcherServlet 视图渲染之前。所以在这个方法中有个 ModelAndView 参数，可以在此做一些修改动作。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在整个请求处理完成后（包括视图渲染）执行，这时做一些资源的清理工作，这个方法只有在 preHandle(……) 被成功执行后并且返回 true才会被执行
    }
}
