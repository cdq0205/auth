package com.imooc.filter;

import com.imooc.pojo.SysUser;
import com.imooc.utils.HttpHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen.d.q
 * @dt 2018/11/5 0005 18:45
 */
@WebFilter(urlPatterns = {"/sys/*","/admin/*"})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        SysUser user = (SysUser)req.getSession().getAttribute("user");
        if (user == null){
            resp.sendRedirect("/signin.jsp");
            return;
        }else{
            HttpHolder.add(user);
            HttpHolder.add(req);
        }

        chain.doFilter(request,response);
        return;
    }

    @Override
    public void destroy() {

    }
}
