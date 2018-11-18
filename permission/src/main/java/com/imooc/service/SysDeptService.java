package com.imooc.service;

import com.google.common.base.Preconditions;
import com.imooc.dao.SysDeptMapper;
import com.imooc.dao.SysUserMapper;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ParamException;
import com.imooc.param.DeptParam;
import com.imooc.pojo.SysDept;
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
 * @dt 2018/10/31 0031 19:57
 */
@Service
@Transactional
@Slf4j
public class SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 创建部门
     * @param deptParam
     */
    public void save(DeptParam deptParam){

        // 名称校验
        if (checkExit(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
            log.error("【创建部门】 失败,同一级别下有相同的部门名称,deptParam = {}",deptParam);
            throw new ParamException(ResultEnum.DEPT_NAME_REPT.getMsg());
        }

        // 数据封装
        SysDept sysDept = (SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build());

        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));

        sysDept.setOperator(HttpHolder.getCurrentUser().getUsername());
        sysDept.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());

        sysDeptMapper.insertSelective(sysDept);
    }

    /**
     * 更新部门
     * @param deptParam
     */
    public void update(DeptParam deptParam){

        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");

        // 名称校验
        if (checkExit(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
            log.error("【更新部门】 失败,同一级别下有相同的部门名称,deptParam = {}",deptParam);
            throw new ParamException(ResultEnum.DEPT_NAME_REPT.getMsg());
        }

        SysDept after = SysDept.builder().id(deptParam.getId()).name(deptParam.getName())
                .parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();

        after.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));

        after.setOperator(HttpHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before,after);
    }

    /**
     * 判断当前部门的子部门是否需要更新
     * @param before
     * @param after
     */
    private void updateWithChild(SysDept before,SysDept after){

        String oldLevelPrefix = before.getLevel();
        String newLevelPrefix = after.getLevel();

        // 子部门需要被更新
        if (!oldLevelPrefix.equals(newLevelPrefix)) {
            // 获取更新前子部门列表
            List<SysDept> childDeptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(childDeptList)){
                for (SysDept childDept : childDeptList){  // 0.2.4 + .3
                    String level = childDept.getLevel(); // 0.2 - .3
                    if (level.length() > oldLevelPrefix.length()){
                        // 更新子部门列表的Level
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        childDept.setLevel(level);
                    }
                }
                // 批量更新子部门列表
                sysDeptMapper.batchUpdateLevel(childDeptList);
            }
        }

        // 更新当前部门的级别
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 校验同一级别下部门名称是否重复
     * @param parentId
     * @param name
     * @param deptId
     * @return
     */
    private boolean checkExit(Integer parentId,String name,Integer deptId){

        return sysDeptMapper.countByNameAndParentId(parentId,name,deptId) > 0;
    }

    /**
     * 获取级别
     * @param parentId
     * @return
     */
    private String getLevel(Integer parentId){

        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(parentId);
        if (sysDept == null){
            return null;
        }
        return sysDept.getLevel();
    }

    /**
     * 删除部门
     * @param deptId
     */
    public void delete(int deptId){

        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept,"待删除的部门不存在,无法删除");

        // 判断当前部门下是否有子部门,有则无法删除
        if (sysDeptMapper.countByParentId(deptId) > 0){
            throw new ParamException("当前部门下存在子部门,无法删除");
        }

        // 判断当前部门下是否有用户,有则无法删除
        if (sysUserMapper.countByDeptId(deptId) > 0 ){
            throw  new ParamException("当前部门下存在用户,无法删除");
        }

        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
