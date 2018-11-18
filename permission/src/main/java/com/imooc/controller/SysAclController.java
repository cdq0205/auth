package com.imooc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.imooc.exception.ParamException;
import com.imooc.param.AclParam;
import com.imooc.pojo.SysAcl;
import com.imooc.pojo.SysRole;
import com.imooc.pojo.SysUser;
import com.imooc.service.SysAclService;
import com.imooc.service.SysRoleService;
import com.imooc.service.SysUserService;
import com.imooc.vo.JsonData;
import com.imooc.vo.PageQuery;
import com.imooc.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 10:19
 */
@RequestMapping("/sys/acl")
@Controller
@Slf4j
public class SysAclController {

    @Autowired
    private SysAclService aclService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 创建权限点
     * @param param
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(@Valid AclParam param, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建权限点】 失败, aclParam = {}",param);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        aclService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限点
     * @param param
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(@Valid AclParam param, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【更新权限点】 失败, aclParam = {}",param);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        aclService.update(param);
        return JsonData.success();
    }

    /**
     * 根据权限模块id分页查询权限点列表
     * @param aclModuleId
     * @param pageQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("aclModuleId") int aclModuleId,
                         @Valid PageQuery pageQuery, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【根据权限模块id分页查询权限点列表】 失败, aclModuleId = {},pageQuery = {}",aclModuleId,pageQuery);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        PageResult<SysAcl> sysAclPageResult = aclService.getPageByAclModuleId(aclModuleId,pageQuery);
        return JsonData.success(sysAclPageResult);
    }

    /**
     * 获取权限点所属的角色和用户
     * @param aclId
     * @return
     */
    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId){

        // 获取当前权限所属的角色列表
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);

        List<SysUser> userList;
       if (CollectionUtils.isEmpty(roleList)){
           userList = Lists.newArrayList();
       }else {
           List<Integer> roleIdList = roleList.stream().map(sysRole -> sysRole.getId()).collect(Collectors.toList());
           userList = sysUserService.getUserListByRoleIdList(roleIdList);
       }

        Map<String,Object> map = Maps.newHashMap();
        map.put("roles",roleList);
        map.put("users",userList);
        return JsonData.success(map);
    }
}
