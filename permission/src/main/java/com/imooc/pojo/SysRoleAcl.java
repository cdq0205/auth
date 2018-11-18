package com.imooc.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SysRoleAcl {
    private Integer id;

    private Integer roleId;

    private Integer aclId;

    private String operator;

    private Date operateTime;

    private String operateIp;


}