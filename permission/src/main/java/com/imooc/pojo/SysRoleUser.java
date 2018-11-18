package com.imooc.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SysRoleUser {
    private Integer id;

    private Integer roleId;

    private Integer userId;

    private String operator;

    private Date operateTime;

    private String operateIp;

}