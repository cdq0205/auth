package com.imooc.dao;

import com.imooc.pojo.SysUser;
import com.imooc.vo.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyword(@Param("keyword") String keyword);

    int countByMail(@Param("mail") String mail,@Param("id") Integer id);

    int countByTelephone(@Param("tel") String tel,@Param("id") Integer id);

    int countByDeptId(@Param("deptId") int deptId);

    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page")PageQuery page);

    List<SysUser> getUserListByUserIdList(@Param("userIdList") List<Integer> userIdList);

    List<SysUser> getAll();
}