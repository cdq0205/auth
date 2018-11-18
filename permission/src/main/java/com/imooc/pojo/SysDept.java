package com.imooc.pojo;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDept {

    public Integer id;

    public String name;

    public Integer parentId;

    public String level;

    public Integer seq;

    public String remark;

    public String operator;

    public Date operateTime;

    public String operateIp;


}