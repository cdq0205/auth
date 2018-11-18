package com.imooc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAclModule {
    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private Integer status;

    private String remark;

    private String operate;

    private Date operateTime;

    private String operateIp;


}