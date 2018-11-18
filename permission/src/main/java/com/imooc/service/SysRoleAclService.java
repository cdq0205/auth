package com.imooc.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.imooc.dao.SysRoleAclMapper;
import com.imooc.pojo.SysRoleAcl;
import com.imooc.utils.HttpHolder;
import com.imooc.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Chen.d.q
 * @dt 2018/11/7 0007 15:08
 */
@Service
@Transactional
@Slf4j
public class SysRoleAclService {

    @Autowired
    private SysRoleAclMapper roleAclMapper;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList){

        List<Integer> originAclIdList = roleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));

        // 判断角色的权限是否被修改
        if (originAclIdList.size() == aclIdList.size()){
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdList.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdList)){
                return;
            }
        }

        // 更新角色的权限列表
        updateRoleAcls(roleId,aclIdList);
    }

    public void updateRoleAcls(Integer roleId,List<Integer> aclIdList){

        roleAclMapper.deleteAclsByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)){
            return ;
        }

        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList){
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(HttpHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest())).operateTime(new Date()).build();

            roleAclList.add(roleAcl);
        }

        roleAclMapper.batchInsert(roleAclList);
    }
}
