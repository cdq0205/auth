package com.imooc.dto;

import com.google.common.collect.Lists;
import com.imooc.pojo.SysDept;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/10/31 0031 20:42
 */

@Data
public class DeptLevelDTO extends SysDept {

    public List<DeptLevelDTO> deptList = Lists.newArrayList();

    /**
     * 数据封装:具有层级的部门
     * @param sysDept
     * @return
     */
   public static DeptLevelDTO adapt(SysDept sysDept){

       DeptLevelDTO deptLevelDTO = new DeptLevelDTO();
       BeanUtils.copyProperties(sysDept,deptLevelDTO);
       return deptLevelDTO;
   }
}
