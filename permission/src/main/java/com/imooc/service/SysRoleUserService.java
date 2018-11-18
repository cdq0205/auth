package com.imooc.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.imooc.dao.SysRoleUserMapper;
import com.imooc.dao.SysUserMapper;
import com.imooc.pojo.SysRoleUser;
import com.imooc.pojo.SysUser;
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
 * @dt 2018/11/7 0007 15:09
 */
@Service
@Transactional
@Slf4j
public class SysRoleUserService {

    @Autowired
    private SysRoleUserMapper roleUserMapper;
    @Autowired
    private SysUserMapper userMapper;

    public List<SysUser> getUserListByRoleId(int roleId){

        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return userMapper.getUserListByUserIdList(userIdList);
    }

    public void changeRoleUsers(int roleId,List<Integer> userIdList){

        List<Integer> originUserIdList = roleUserMapper.getUserIdListByRoleId(roleId);

        // 判断角色的权限是否被修改
        if (originUserIdList.size() == userIdList.size()){
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)){
                return;
            }
        }

        // 更新角色的权限列表
        updateRoleUsers(roleId,userIdList);
    }

    public void updateRoleUsers(int roleId,List<Integer> userIdList){

        roleUserMapper.deleteUsersByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)){
            return ;
        }

        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList){
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(HttpHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest())).operateTime(new Date()).build();

            roleUserList.add(roleUser);
        }

        roleUserMapper.batchInsert(roleUserList);
    }

    public List<Integer> getRoleIdListByUserId(int userId){

        return roleUserMapper.getRoleIdListByUserId(userId);
    }
}
