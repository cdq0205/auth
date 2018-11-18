package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Chen.d.q
 * @dt 2018/11/3 0003 12:54
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.page")
    public ModelAndView index(){

        return new ModelAndView("admin");
    }
}
