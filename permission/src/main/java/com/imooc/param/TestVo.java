package com.imooc.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Chen.d.q
 * @dt 2018/10/30 0030 21:43
 */
@Data
public class TestVo {

    @NotBlank(message = "空空如也")
    private String msg;

    @NotNull(message = "空空如也")
    @Max(value = 10,message = "不能超过10哦")
    @Min(value = 1,message = "不能低于1哦")
    private Integer id;

}
