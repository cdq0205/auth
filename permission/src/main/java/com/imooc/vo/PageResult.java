package com.imooc.vo;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/5 0005 11:22
 */
@Data
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total;
}
