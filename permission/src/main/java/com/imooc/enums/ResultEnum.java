package com.imooc.enums;

import lombok.Getter;

/**
 * @author Chen.d.q
 * @dt 2018/10/31 0031 19:52
 */
@Getter
public enum ResultEnum {

    PARAM_ERROR("参数不正确"),

    DEPT_NAME_REPT("同一级别下部门名称重复"),

    USER_REGISTER_MAIL_EXIST("用户注册邮箱已存在"),

    USER_REGISTER_TEL_EXIST("用户注册电话号码已存在"),

    ACL_MODULE_NAME_REPT("同一级别下权限模块名称重复"),

    ACL_NAME_REPT("同一级别下权限点名称重复"),

    ROLE_NAME_REPT("角色名称重复");

    private String msg;

    ResultEnum(String msg) {
        this.msg = msg;
    }
}
