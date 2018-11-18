package com.imooc.controller;

import com.google.common.collect.Maps;
import com.imooc.dto.AclModuleLevelDTO;
import com.imooc.exception.ParamException;
import com.imooc.param.UserParam;
import com.imooc.pojo.SysRole;
import com.imooc.pojo.SysUser;
import com.imooc.service.SysRoleService;
import com.imooc.service.SysTreeService;
import com.imooc.service.SysUserService;
import com.imooc.vo.JsonData;
import com.imooc.vo.PageQuery;
import com.imooc.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Chen.d.q
 * @dt 2018/11/3 0003 11:55
 */
@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 创建用户
     * @param userParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(@Valid UserParam userParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建用户】 失败, userParam = {}",userParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        sysUserService.save(userParam);
        return JsonData.success();
    }

    /**
     * 更新用户
     * @param userParam
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(@Valid UserParam userParam, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【更新用户】 失败, userParam = {}",userParam);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        sysUserService.update(userParam);
        return JsonData.success();
    }

    /**
     * 根据部门id分页查询用户信息
     * @param deptId
     * @param pageQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId,
                         @Valid PageQuery pageQuery,BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【根据部门id分页查询用户信息】 失败, deptId = {},pageQuery = {}",deptId,pageQuery);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        PageResult<SysUser> sysUserPageResult = sysUserService.getPageByDeptId(deptId,pageQuery);
        return JsonData.success(sysUserPageResult);
    }

    /**
     * 获取用户所具有的角色和权限点
     * @param userId
     * @return
     */
    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId){

        // 获取用户对应的权限树
        List<AclModuleLevelDTO> aclModuleLevelDTOList = sysTreeService.userAclTree(userId);
        // 获取用户对应的角色列表
        List<SysRole> roleList = sysRoleService.getRoleListByUserId(userId);

        Map<String,Object> map = Maps.newHashMap();
        map.put("acls",aclModuleLevelDTOList);
        map.put("roles",roleList);

        return JsonData.success(map);
    }
}
