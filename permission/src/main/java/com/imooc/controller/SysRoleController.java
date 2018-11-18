package com.imooc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.imooc.exception.ParamException;
import com.imooc.param.RoleParam;
import com.imooc.pojo.SysRole;
import com.imooc.pojo.SysUser;
import com.imooc.service.*;
import com.imooc.utils.StringUtil;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 19:13
 */
@RequestMapping("/sys/role")
@Controller
@Slf4j
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysTreeService treeService;
    @Autowired
    private SysRoleAclService roleAclService;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysUserService userService;


    @RequestMapping("/role.page")
    public ModelAndView page(){

        return new ModelAndView("role");
    }

    /**
     * 创建角色
     * @param param
     * @param bindingResult
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(@Valid RoleParam param, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【创建角色】 失败, roleParam = {}",param);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        roleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新角色
     * @param param
     * @param bindingResult
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(@Valid RoleParam param, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.error("【更新角色】 失败, roleParam = {}",param);
            throw new ParamException(bindingResult.getFieldError().getDefaultMessage());
        }

        roleService.update(param);
        return JsonData.success();
    }

    /**
     * 获取角色列表
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData<SysRole> list(){

        List<SysRole> roleList = roleService.getAll();
        return JsonData.success(roleList);
    }

    /**
     * 获取当前角色/权限树
     * @return
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId){

        return JsonData.success(treeService.roleTree(roleId));
    }

    /**
     * 修改角色的权限
     * @return
     */
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "aclIds",required = false,defaultValue = "") String aclIds){

        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        roleAclService.changeRoleAcls(roleId,aclIdList);
        return JsonData.success();
    }

    /**
     * 获取角色/用户
     * 所有用户 = 选中用户 + 未选中用户
     * @param roleId
     * @return
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId){

        List<SysUser> selectedUserList = roleUserService.getUserListByRoleId(roleId);
        List<SysUser> allUserList = userService.getAll();
        List<SysUser> unselectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser->sysUser.getId()).collect(Collectors.toSet());

        for (SysUser user : allUserList){
            if (user.getStatus() ==1 && !selectedUserIdSet.contains(user.getId())){
                unselectedUserList.add(user);
            }
        }

        Map<String,List<SysUser>> map = Maps.newHashMap();
        map.put("selected",selectedUserList);
        map.put("unselected",unselectedUserList);

        return JsonData.success(map);
    }

    /**
     * 修改用户角色
     * @return
     */
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "userIds",required = false,defaultValue = "") String userIds){

        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        roleUserService.changeRoleUsers(roleId,userIdList);
        return JsonData.success();
    }
}
