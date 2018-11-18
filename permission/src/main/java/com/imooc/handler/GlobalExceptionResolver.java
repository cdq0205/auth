package com.imooc.handler;

import com.imooc.exception.ParamException;
import com.imooc.vo.JsonData;
import com.imooc.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen.d.q
 * @dt 2018/10/30 0030 17:40
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver{

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object permissionExceptionHandler(RuntimeException ex,HttpServletRequest request){

        String url = request.getRequestURL().toString();
        ModelAndView mav = null;
        String defaultMsg = "system error";

        if (url.endsWith(".json")){
            if (ex instanceof PermissionException || ex instanceof ParamException){
                log.error("【全局异常处理】 自定义异常(json), url:{}, msg:{}",url,ex.getMessage());
                return JsonData.error(ex.getMessage());
            }else{
                log.error("【全局异常处理】 未知异常(json), url:{}, msg:{}",url,ex.getMessage());
                return JsonData.error(defaultMsg);
            }
        }else if (url.endsWith(".page")){
            log.error("【全局异常处理】 未知异常(page), url:{}, msg:{}",url,ex.getMessage());
            JsonData result = JsonData.error(defaultMsg);
            mav = new ModelAndView("exception",result.toMap());
            return mav;
        }else{
            log.error("【全局异常处理】 未知异常(其他), url:{}, msg:{}",url,ex.getMessage());
            return JsonData.error(defaultMsg);
        }
    }
}
