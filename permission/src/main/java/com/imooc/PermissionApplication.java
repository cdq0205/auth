package com.imooc;

import com.imooc.dao.SysAclModuleMapper;
import com.imooc.exception.ParamException;
import com.imooc.param.TestVo;
import com.imooc.pojo.SysAclModule;
import com.imooc.utils.ApplicationContextHelper;
import com.imooc.utils.HttpInterceptor;
import com.imooc.utils.JsonMapper;
import com.imooc.utils.ValidatorUtil;
import com.imooc.vo.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;

@SpringBootApplication
@RestController
@Slf4j
@ServletComponentScan   // 处理 servlet,filter,listener
@MapperScan("com.imooc.dao")
public class PermissionApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(PermissionApplication.class);
    }

    /**
     * 自定义拦截器配置
     * @return
     */
    @Bean
    public WebMvcConfigurer interceptorConfigurer(){

        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {

                registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/sys/**");
                super.addInterceptors(registry);
            }
        };
    }

    /**
     * 访问json错误测试
     * @return
     */
    @GetMapping("/index.json")
    public JsonData index(){

//        throw new RuntimeException("json runtime exception");

//        throw new PermissionException("json customer exception");

        return JsonData.success("hello permission");
    }

    /**
     * 访问page错误测试
     * @return
     */
    @GetMapping("/index.page")
    public JsonData page(){

        throw new RuntimeException("page runtime exception");
    }

    /**
     * 自定义参数校验测试
     * @param vo
     * @return
     * @throws ParamException
     */
    @GetMapping("/validate.json")
    public JsonData validate(TestVo vo) throws ParamException {

        log.info("【validate】 ");

        ValidatorUtil.check(vo);

        return JsonData.success("【test validate】");
    }

    /**
     * 简单参数校验测试
     * @param vo
     * @param bindingResult
     * @return
     */
    @GetMapping("/validate2.json")
    public JsonData validate2(@Valid TestVo vo, BindingResult bindingResult)  {

        log.info("【validate2】");

        if (bindingResult.hasErrors()){
            log.error("【参数校验】 失败,vo = {}",vo);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        /** 上下文工具类 + 数据转换器测试.*/
        SysAclModuleMapper sysAclModuleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(1);
        log.info("【ApplicationContextHelper + JsonMapper】 测试,{}",JsonMapper.obj2String(sysAclModule));

        return JsonData.success("【test validate2】");
    }

}
