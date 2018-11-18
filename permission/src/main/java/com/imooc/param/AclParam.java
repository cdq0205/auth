package com.imooc.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 13:34
 */
@Data
public class AclParam {

    private Integer id;

    @NotBlank(message = "权限点名称不能为空")
    @Length(min = 2,max = 20,message = "权限点名称长度在2~20个字符之间")
    private String name;

    @NotNull(message = "必须指定权限点所在权限模块")
    private Integer aclModuleId;

    @Length(min = 6,max = 50,message = "权限点url长度在6~50个字符之间")
    private String url;

    @NotNull(message = "必须指定权限点的类型")
    @Min(value = 1,message = "权限点的类型不合法")
    @Max(value = 3,message = "权限点的类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点的状态")
    @Min(value = 0,message = "权限点的状态不合法")
    @Max(value = 1,message = "权限点的状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的序列")
    private Integer seq;

    @Length(max = 200,message = "权限点的备注在200个字符以内")
    private String remark;
}
