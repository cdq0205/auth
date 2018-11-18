package com.imooc.dao;

import com.imooc.pojo.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int countByNameAndId(@Param("name") String name,@Param("id") Integer id);

    List<SysRole> getAll();

    List<SysRole> getByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}