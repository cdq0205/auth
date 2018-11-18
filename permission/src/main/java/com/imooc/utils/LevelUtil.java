package com.imooc.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 级别工具类
 * @author Chen.d.q
 * @dt 2018/10/31 0031 20:08
 */
public class LevelUtil {

    /** 分隔符.*/
    public static final String SEPARATOR = ".";

    /** 最顶级*/
    public static final String ROOT = "0";

    /**
     * 计算级别
     * @param parentLevel
     * @param parentId
     * @return
     */
    public static String calculateLevel(String parentLevel,Integer parentId){

        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }
        return StringUtils.join(parentLevel,SEPARATOR,parentId);
    }
}
