package com.imooc.utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * 数据转换器
 * @author Chen.d.q
 * @dt 2018/10/31 0031 14:13
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * object 2 string
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T t){

        if (t == null){
            return null;
        }
        try {
            return t instanceof String ? (String)t : objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            log.error("【object -> string】 转换错误,param = {},error = {}",t,e.getMessage());
            return null;
        }
    }

    /**
     * string 2 object
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T str2Object(String str, TypeReference<T> typeReference){

        if (str == null || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.error("【string -> object】 转换错误,param = {},paramType = {},error = {}",str,typeReference,e.getMessage());
            return null;
        }
    }
}
