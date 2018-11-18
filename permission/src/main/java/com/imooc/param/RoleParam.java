package com.imooc.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Chen.d.q
 * @dt 2018/11/6 0006 19:14
 */

@Data
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @Length(min = 2,max = 20,message = "角色名称长度在2~20个字符之间")
    private String name;

    @Min(value = 1,message = "角色的类型不合法")
    @Max(value = 2,message = "角色的类型不合法")
    private Integer type = 1;

    @NotNull(message = "必须指定角色的状态")
    @Min(value = 0,message = "角色的状态不合法")
    @Max(value = 1,message = "角色的状态不合法")
    private Integer status;

    @Length(max = 200,message = "角色的备注在200个字符以内")
    private String remark = "";
}
