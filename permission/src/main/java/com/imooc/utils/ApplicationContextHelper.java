package com.imooc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 获取上下文工具类
 * @author Chen.d.q
 * @dt 2018/10/31 0031 14:42
 */

@Configuration
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        applicationContext = context;
    }

    public static <T> T popBean(Class<T> clazz){

        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static <T> T popBean(String str, Class<T> clazz){

        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(str,clazz);
    }
}
