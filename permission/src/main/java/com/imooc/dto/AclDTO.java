package com.imooc.dto;

import com.imooc.pojo.SysAcl;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Chen.d.q
 * @dt 2018/11/7 0007 10:56
 */
@Data
public class AclDTO extends SysAcl {

    private boolean checked = false;

    private boolean hasAcl = false;

    public static AclDTO adapt(SysAcl acl){

        AclDTO aclDTO = new AclDTO();
        BeanUtils.copyProperties(acl,aclDTO);
        return aclDTO;
    }
}
