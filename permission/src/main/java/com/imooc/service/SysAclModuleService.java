package com.imooc.service;

import com.google.common.base.Preconditions;
import com.imooc.dao.SysAclMapper;
import com.imooc.dao.SysAclModuleMapper;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ParamException;
import com.imooc.param.AclModuleParam;
import com.imooc.pojo.SysAclModule;
import com.imooc.utils.HttpHolder;
import com.imooc.utils.IpUtil;
import com.imooc.utils.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 10:45
 */
@Service
@Transactional
@Slf4j
public class SysAclModuleService {

    @Autowired
    private SysAclModuleMapper aclModuleMapper;
    @Autowired
    private SysAclMapper aclMapper;

    /**
     * 创建权限模块
     * @param param
     */
    public void save(AclModuleParam param){

        // 名称校验
        if (checkExit(param.getParentId(),param.getName(),param.getId())){
            log.error("【创建权限模块】 失败,同一级别下有相同的权限模块名称,aclModuleParam = {}",param);
            throw new ParamException(ResultEnum.ACL_MODULE_NAME_REPT.getMsg());
        }

        SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).build();

        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));

        aclModule.setOperate(HttpHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());

        aclModuleMapper.insertSelective(aclModule);
    }

    /**
     * 更新权限模块
     * @param param
     */
    public void update(AclModuleParam param){

        SysAclModule before = aclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限模块不存在");

        // 名称校验
        if (checkExit(param.getParentId(),param.getName(),param.getId())){
            log.error("【创建权限模块】 失败,同一级别下有相同的权限模块名称,aclModuleParam = {}",param);
            throw new ParamException(ResultEnum.ACL_MODULE_NAME_REPT.getMsg());
        }

        SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).status(param.getStatus()).build();

        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));

        after.setOperate(HttpHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before,after);
    }

    /**
     * 判断当前权限模块的子模块是否需要更新
     * @param before
     * @param after
     */
    private void updateWithChild(SysAclModule before,SysAclModule after){

        String oldLevelPrefix = before.getLevel();
        String newLevelPrefix = after.getLevel();

        // 子部门需要被更新
        if (!oldLevelPrefix.equals(newLevelPrefix)) {
            // 获取更新前子部门列表
            List<SysAclModule> childAclModuleList = aclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(childAclModuleList)){
                for (SysAclModule childAclModule : childAclModuleList){  // 0.2.4 + .3
                    String level = childAclModule.getLevel(); // 0.2 - .3
                    if (level.length() > oldLevelPrefix.length()){
                        // 更新子部门列表的Level
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        childAclModule.setLevel(level);
                    }
                }
                // 批量更新子部门列表
                aclModuleMapper.batchUpdateLevel(childAclModuleList);
            }
        }

        // 更新当前权限模块的级别
        aclModuleMapper.updateByPrimaryKey(after);

    }

    /**
     * 校验同一级别下权限模块名称是否重复
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    private boolean checkExit(Integer parentId,String name,Integer id){

        return aclModuleMapper.countByNameAndParentId(parentId,name,id) > 0;
    }

    /**
     * 获取级别
     * @param parentId
     * @return
     */
    private String getLevel(Integer parentId){

        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(parentId);
        if (aclModule == null){
            return null;
        }
        return aclModule.getLevel();
    }

    /**
     * 删除权限模块
     * @param aclModuleId
     */
    public void delete(int aclModuleId){

        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule,"待删除的权限模块不存在,无法删除");

        // 判断当前权限模块下是否有子权限模块,有则无法删除
        if (aclModuleMapper.countByParentId(aclModuleId) > 0){
            throw new ParamException("当前权限模块下存在子权限模块,,无法删除");
        }

        // 判断当前部门下是否有用户,有则无法删除
        if (aclMapper.countByAclModuleId(aclModuleId) > 0 ){
            throw  new ParamException("当前权限模块下存在权限点,无法删除");
        }

        aclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }
}
