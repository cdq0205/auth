package com.imooc.controller;

import com.imooc.pojo.SysUser;
import com.imooc.service.SysUserService;
import com.imooc.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen.d.q
 * @dt 2018/11/3 0003 12:32
 */
@Controller
@RequestMapping
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException{

        request.getSession().invalidate();
        response.sendRedirect("signin.jsp");
    }

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {

        String username = request.getParameter("username");  // mail /telephone
        String password = request.getParameter("password");
        String ret = request.getParameter("ret");  // redirectUrl

        SysUser user = sysUserService.findByKeyword(username);
        String errorMsg = "";

        if (StringUtils.isEmpty(username)){
            errorMsg = "用户名不能为空";
        }else if (StringUtils.isEmpty(password)){
            errorMsg = "密码不能为空";
        }else if (user == null){
            errorMsg = "用户不存在";
        }else if (!user.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else if (user.getStatus()!=1){
            errorMsg = "用户被冻结,请联系管理员";
        }else {
            // login success
            request.getSession().setAttribute("user",user);
            if (StringUtils.isNotBlank(ret)){
                response.sendRedirect(ret);
            }else{
                response.sendRedirect("/admin/index.page");
            }
        }

        // login fail
        request.setAttribute("username",username);
        request.setAttribute("error",errorMsg);
        if (StringUtils.isNotBlank(ret)){
            request.setAttribute("ret",ret);
        }
        request.getRequestDispatcher("signin.jsp").forward(request,response);
    }
}
