package com.imooc.utils;

import com.imooc.pojo.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理当前进程的请求用户信息
 *      ThreadLocal <==> Map,map的key是当前进程
 * @author Chen.d.q
 * @dt 2018/11/5 0005 18:27
 */
public class HttpHolder {

    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    /**
     * 设置当前进程的用户信息
     * @param user
     */
    public static void  add(SysUser user){

        userHolder.set(user);
    }

    /**
     * 设置当前进程的请求信息
     * @param request
     */
    public static void add(HttpServletRequest request){

        requestHolder.set(request);
    }

    /**
     * 获取当前进程的用户信息
     * @return
     */
    public static SysUser getCurrentUser(){

       return userHolder.get();
    }

    /**
     * 获取当前进程的请求信息
     * @return
     */
    public static HttpServletRequest getCurrentRequest(){

        return requestHolder.get();
    }

    /**
     * 释放当前进程的请求用户信息
     */
    public static void removeThreadLocalInfo(){

        userHolder.remove();
        requestHolder.remove();
    }

}
