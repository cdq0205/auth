package com.imooc.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Chen.d.q
 * @dt 2018/10/31 0031 19:32
 */
@Data
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(max = 20,min = 2,message = "部门名称长度需要在2~20个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "部门序列不能为空")
    private Integer seq;

    @Length(max = 200,message = "部门备注信息长度不能超过200")
    private String remark = "";

}
