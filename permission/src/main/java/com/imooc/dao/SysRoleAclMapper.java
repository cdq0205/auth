package com.imooc.dao;

import com.imooc.pojo.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteAclsByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}