package com.imooc.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.imooc.dao.SysRoleUserMapper;
import com.imooc.dao.SysUserMapper;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ParamException;
import com.imooc.param.UserParam;
import com.imooc.pojo.SysUser;
import com.imooc.utils.HttpHolder;
import com.imooc.utils.IpUtil;
import com.imooc.utils.MD5Util;
import com.imooc.utils.PasswordUtil;
import com.imooc.vo.PageQuery;
import com.imooc.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Chen.d.q
 * @dt 2018/11/3 0003 11:05
 */
@Service
@Slf4j
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    /**
     * 用户注册
     * @param param
     */
    public void save(UserParam param){

        if (checkMailExist(param.getMail(),param.getId())){
            log.error("【用户注册】 失败,邮箱已存在! mail = {}",param.getMail());
            throw new ParamException(ResultEnum.USER_REGISTER_MAIL_EXIST.getMsg());
        }

        if (checkTelExist(param.getTelephone(),param.getId())){
            log.error("【用户注册】 失败,电话号码已存在! telephone = {}",param.getTelephone());
            throw new ParamException(ResultEnum.USER_REGISTER_TEL_EXIST.getMsg());
        }

        String password = PasswordUtil.randomPassword();
        //TODO sendMsg
        password = "123456";
        String encryptPassword = MD5Util.encrypt(password);

        SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone())
                .mail(param.getMail()).password(encryptPassword).deptId(param.getDeptId()).status(param.getStatus())
                .remark(param.getRemark()).build();

        user.setOperator(HttpHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        //TODO sendMsg

        sysUserMapper.insertSelective(user);
    }

    /**
     * 用户更新
     * @param param
     */
    public void update(UserParam param){

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");

        if (checkMailExist(param.getMail(),param.getId())){
            log.error("【用户更新】 失败,邮箱已存在! mail = {}",param.getMail());
            throw new ParamException(ResultEnum.USER_REGISTER_MAIL_EXIST.getMsg());
        }

        if (checkTelExist(param.getTelephone(),param.getId())){
            log.error("【用户更新】 失败,电话号码已存在! telephone = {}",param.getTelephone());
            throw new ParamException(ResultEnum.USER_REGISTER_TEL_EXIST.getMsg());
        }

        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId())
                .status(param.getStatus()).remark(param.getRemark()).build();

        after.setOperator(HttpHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getUserIP(HttpHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 邮箱校验
     * @param mail
     * @param id
     * @return
     */
    public boolean checkMailExist(String mail,Integer id){

        return sysUserMapper.countByMail(mail,id)>0;
    }

    /**
     * 电话校验
     * @param tel
     * @param id
     * @return
     */
    public boolean checkTelExist(String tel,Integer id){

        return sysUserMapper.countByTelephone(tel,id)>0;
    }

    /**
     * 邮箱/电话登录
     * @param keyword
     * @return
     */
    public SysUser findByKeyword(String keyword){

        return sysUserMapper.findByKeyword(keyword);
    }

    /**
     * 根据部门id分页查询用户信息
     * @param deptId
     * @param pageQuery
     * @return
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery){

        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0 ){
            List<SysUser> userList = sysUserMapper.getPageByDeptId(deptId,pageQuery);
            return PageResult.<SysUser>builder().total(count).data(userList).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    public List<SysUser> getAll(){

        return sysUserMapper.getAll();
    }

    public List<SysUser> getUserListByRoleIdList(List<Integer> roleIdList){

        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.getUserListByUserIdList(userIdList);
    }

}
