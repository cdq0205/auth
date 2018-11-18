package com.imooc.dto;

import com.google.common.collect.Lists;
import com.imooc.pojo.SysAclModule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 12:05
 */
@Data
public class AclModuleLevelDTO extends SysAclModule {

    public List<AclModuleLevelDTO> aclModuleList = Lists.newArrayList();

    public List<AclDTO> aclList = Lists.newArrayList();

    /**
     * 数据封装:具有层级的部门
     * @param sysAclModule
     * @return
     */
    public static AclModuleLevelDTO adapt(SysAclModule sysAclModule){

        AclModuleLevelDTO aclModuleLevelDTO = new AclModuleLevelDTO();
        BeanUtils.copyProperties(sysAclModule,aclModuleLevelDTO);
        return aclModuleLevelDTO;
    }
}
