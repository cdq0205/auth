package com.imooc.dao;

import com.imooc.pojo.SysAcl;
import com.imooc.vo.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("name") String name, @Param("id") Integer id);

    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page")PageQuery page);

    List<SysAcl> getAll();

    List<SysAcl> getAclListByAclIdList(@Param("aclIdList")List<Integer> aclIdList);
}