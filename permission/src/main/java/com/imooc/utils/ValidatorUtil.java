package com.imooc.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.imooc.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 自定义参数校验工具类
 * @author Chen.d.q
 * @dt 2018/10/30 0030 21:11
 */
public class ValidatorUtil {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 最终的参数校验方法
     *      封装了错误的field和相应的提示msg
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    public static <T> Map<String,String> validate(T t,Class...groups){

        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t,groups);
        if (validateResult.isEmpty()){
            return Collections.emptyMap();
        }else{
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            while (iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation)iterator.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String,String> validateList(Collection<?> collection){

        Preconditions.checkNotNull(collection);

        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()){
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object,new Class[0]);
        }while (errors.isEmpty());

        return errors;
    }

    public static Map<String,String> validateObject(Object first,Object...objects){

       if (objects != null && objects.length > 0){
           return validateList(Lists.asList(first,objects));
       }else {
           return validate(first,new Class[0]);
       }
    }

    public static void check(Object object) throws ParamException{

        Map errors = ValidatorUtil.validateObject(object);
        if (MapUtils.isNotEmpty(errors)){
            throw new ParamException(errors.toString());
        }
    }
}
