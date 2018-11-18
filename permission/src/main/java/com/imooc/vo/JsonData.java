package com.imooc.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * http请求返回的数据封装
 * @author Chen.d.q
 * @dt 2018/10/30 0030 17:07
 */
@Data
public class JsonData<T> {

    /** 状态码.*/
    private boolean ret;

    /** 提示信息.*/
    private String msg;

    /** 数据内容.*/
    private T data;

    public static JsonData success(Object object,String msg){

        JsonData jsonData = new JsonData();
        jsonData.ret = true;
        jsonData.msg = msg;
        jsonData.data = object;
        return jsonData;
    }

    public static JsonData success(Object object){

        return success(object,null);
    }

    public static JsonData success(){

        return success(null,null);
    }

    public static JsonData error(String msg){

        JsonData jsonData = new JsonData();
        jsonData.ret = false;
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String,Object> toMap(){

        HashMap map = new HashMap();
        map.put("ret",ret);
        map.put("msg",msg);
        map.put("data",data);
        return map;
    }
}
