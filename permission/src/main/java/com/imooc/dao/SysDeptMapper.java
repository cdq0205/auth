package com.imooc.dao;

import com.imooc.pojo.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("deptId") Integer deptId);

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("childDeptList") List<SysDept> childDeptList);

    int countByParentId(@Param("deptId") int deptId);
}