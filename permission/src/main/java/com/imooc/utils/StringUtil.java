package com.imooc.utils;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen.d.q
 * @dt 2018/11/7 0007 14:55
 */
public class StringUtil {

    /**
     * str = "1,2,3,4,5"
     * [1,2,3,4,5]
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str){

        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
