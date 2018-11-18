package com.imooc.controller;

import com.imooc.dto.DeptLevelDTO;
import com.imooc.exception.ParamException;
import com.imooc.param.DeptParam;
import com.imooc.service.SysDeptService;
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
 * @dt 2018/10/31 0031 19:47
 */
@RequestMapping("/sys/dept")
@Controller
@Slf4j
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page(){

        return new ModelAndView("dept");
    }

    /**
     * 创建部门
     * @param deptParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData  save(@Valid DeptParam deptParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建部门】 失败, deptParam = {}",deptParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    /**
     * 更新部门
     * @param deptParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(@Valid DeptParam deptParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【更新部门】 失败, deptParam = {}",deptParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        sysDeptService.update(deptParam);
        return JsonData.success();
    }

    /**
     * 部门树状图
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){

        List<DeptLevelDTO> deptLevelDTOS = sysTreeService.deptTree();
        return JsonData.success(deptLevelDTOS);
    }

    /**
     * 删除部门
     * @param deptId
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int deptId){

        sysDeptService.delete(deptId);
        return JsonData.success();
    }
}
