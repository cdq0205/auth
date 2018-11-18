package com.imooc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Chen.d.q
 * @dt 2018/10/31 0031 18:03
 */
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    private static final String REQUEST_START_TIME = "REQUEST_START_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI().toString();
        Map paramMap = request.getParameterMap();
        log.info("【拦截前】 ,uri = {}, paramMap = {}" , uri,JsonMapper.obj2String(paramMap));

        request.setAttribute(REQUEST_START_TIME,System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

//        String uri = request.getRequestURI().toString();
//        Map paramMap = request.getParameterMap();
//        log.info("【拦截后】 ,uri = {}, paramMap = {}" , uri,JsonMapper.obj2String(paramMap));

        log.info("【拦截后】 耗时:{}ms", (System.currentTimeMillis() - (Long) request.getAttribute(REQUEST_START_TIME)));

        HttpHolder.removeThreadLocalInfo();
    }
}
