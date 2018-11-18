package com.imooc.service;

import com.google.common.base.Preconditions;
import com.imooc.dao.SysAclMapper;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ParamException;
import com.imooc.param.AclParam;
import com.imooc.pojo.SysAcl;
import com.imooc.utils.HttpHolder;
import com.imooc.utils.IpUtil;
import com.imooc.vo.PageQuery;
import com.imooc.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 13:51
 */
@Service
@Transactional
@Slf4j
public class SysAclService {

    @Autowired
    private SysAclMapper aclMapper;

    /**
     * 创建权限点
     * @param param
     */
    public void save(AclParam param){

        // 名称校验
        if (checkExit(param.getAclModuleId(),param.getName(),param.getId())){
            log.error("【创建权限点】 失败,同一级别下有相同的权限点名称,aclParam = {}",param);
            throw new ParamException(ResultEnum.ACL_NAME_REPT.getMsg());
        }

        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();

        acl.setCode(generatedCode());
        acl.setOperator(HttpHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());

        aclMapper.insertSelective(acl);
    }

    /**
     * 更新权限点
     * @param param
     */
    public void update(AclParam param){

        SysAcl before = aclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");

        // 名称校验
        if (checkExit(param.getAclModuleId(),param.getName(),param.getId())){
            log.error("【更新权限点】 失败,同一级别下有相同的权限点名称,aclParam = {}",param);
            throw new ParamException(ResultEnum.ACL_NAME_REPT.getMsg());
        }

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();

        after.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        after.setOperator(HttpHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());

        aclMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 校验同一级别下权限点名称是否重复
     * @param aclModuleId
     * @param name
     * @param id
     * @return
     */
    private boolean checkExit(Integer aclModuleId,String name,Integer id){

        return aclMapper.countByNameAndAclModuleId(aclModuleId,name,id) > 0;
    }

    public String generatedCode(){

        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + (int) (Math.random()*100);
    }

    /**
     * 根据权限模块id分页查询权限点列表
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery){

        int count = aclMapper.countByAclModuleId(aclModuleId);
        if (count > 0 ){
            List<SysAcl> aclList = aclMapper.getPageByAclModuleId(aclModuleId,pageQuery);
            return PageResult.<SysAcl>builder().total(count).data(aclList).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
