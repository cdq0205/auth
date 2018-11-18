package com.imooc.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.imooc.dao.SysRoleAclMapper;
import com.imooc.dao.SysRoleMapper;
import com.imooc.dao.SysRoleUserMapper;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ParamException;
import com.imooc.param.RoleParam;
import com.imooc.pojo.SysRole;
import com.imooc.utils.HttpHolder;
import com.imooc.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 19:13
 */
@Service
@Transactional
@Slf4j
public class SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    /**
     * 创建角色
     * @param param
     */
    public void save(RoleParam param) {

        // 名称校验
        if (checkExit(param.getName(),param.getId())){
            log.error("【创建角色】 失败,角色名称重复,roleParam = {}",param);
            throw new ParamException(ResultEnum.ROLE_NAME_REPT.getMsg());
        }

        SysRole role = SysRole.builder().name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();

        role.setOperator(HttpHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        roleMapper.insertSelective(role);

    }

    /**
     * 更新角色
     * @param param
     */
    public void update(RoleParam param) {

        SysRole before = roleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");

        // 名称校验
        if (checkExit(param.getName(),param.getId())){
            log.error("【更新角色】 失败,角色名称重复,roleParam = {}",param);
            throw new ParamException(ResultEnum.ROLE_NAME_REPT.getMsg());
        }

        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();

        after.setOperator(HttpHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        roleMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 校验角色名称是否重复
     * @param name
     * @param id
     * @return
     */
    private boolean checkExit(String name,Integer id){

        return roleMapper.countByNameAndId(name,id) > 0;
    }

    /**
     * 获取角色列表
     * @return
     */
    public List<SysRole> getAll(){

        return roleMapper.getAll();
    }

    /**
     * 通过用户id获取角色列表
     * @param userId
     * @return
     */
    public List<SysRole> getRoleListByUserId(int userId){

        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return roleMapper.getByRoleIdList(roleIdList);
    }

    public List<SysRole> getRoleListByAclId(int aclId){

        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return roleMapper.getByRoleIdList(roleIdList);
    }
}