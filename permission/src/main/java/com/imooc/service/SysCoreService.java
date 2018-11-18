package com.imooc.service;

import com.google.common.collect.Lists;
import com.imooc.dao.SysAclMapper;
import com.imooc.dao.SysRoleAclMapper;
import com.imooc.dao.SysRoleUserMapper;
import com.imooc.pojo.SysAcl;
import com.imooc.utils.HttpHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/7 0007 11:47
 */
@Service
@Transactional
@Slf4j
public class SysCoreService {

    @Autowired
    private SysAclMapper aclMapper;
    @Autowired
    private SysRoleUserMapper roleUserMapper;
    @Autowired
    private SysRoleAclMapper roleAclMapper;

    /**
     * 获取当前用户的权限列表
     * @return
     */
    List<SysAcl> getCurrentUserAclList(){

        int userId = HttpHolder.getCurrentUser().getId();
        List<SysAcl> aclList = getUserAclList(userId);
        return aclList;
    }

    /**
     * 获取角色的权限列表
     * @param roleId
     * @return
     */
    List<SysAcl> getRoleAclList(int roleId){

        List<Integer> aclIdList = roleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        return aclMapper.getAclListByAclIdList(aclIdList);
    }

    /**
     * 获取用户的权限列表
     * @param userId
     * @return
     */
    List<SysAcl> getUserAclList(int userId){

        if (isSuperAdmin()){
            List<SysAcl> aclList = aclMapper.getAll();
            return aclList;
        }
        List<Integer> roleIdList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        List<Integer> aclIdList = roleAclMapper.getAclIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        List<SysAcl> aclList = aclMapper.getAclListByAclIdList(aclIdList);
        return aclList;
    }

    public boolean isSuperAdmin(){

        return true;
    }


}
