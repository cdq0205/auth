package com.imooc.controller;

import com.imooc.dto.AclModuleLevelDTO;
import com.imooc.exception.ParamException;
import com.imooc.param.AclModuleParam;
import com.imooc.service.SysAclModuleService;
import com.imooc.service.SysTreeService;
import com.imooc.vo.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 10:19
 */
@RequestMapping("/sys/aclModule")
@Controller
@Slf4j
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService aclModuleService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page(){

        return new ModelAndView("acl");
    }

    /**
     * 创建权限模块
     * @param aclModuleParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(@Valid AclModuleParam aclModuleParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建权限模块】 失败, aclModuleParam = {}",aclModuleParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }
        aclModuleService.save(aclModuleParam);
        return JsonData.success();
    }

    /**
     * 更新权限模块
     * @param aclModuleParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(@Valid AclModuleParam aclModuleParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【更新权限模块】 失败, aclModuleParam = {}",aclModuleParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        aclModuleService.update(aclModuleParam);
        return JsonData.success();
    }

    /**
     * 权限模块树状图
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){

        List<AclModuleLevelDTO> aclModuleLevelDTOList = sysTreeService.aclModuleTree();
        return JsonData.success(aclModuleLevelDTOList);
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int aclModuleId){

        aclModuleService.delete(aclModuleId);
        return JsonData.success();
    }
}
