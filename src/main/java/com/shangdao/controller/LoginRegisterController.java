package com.shangdao.controller;

import com.shangdao.domain.user.User;
import com.shangdao.service.UserService;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginRegisterController {
    @Autowired
    UserService userService;
    @PostMapping("/frontLogin")
    @ResponseBody
    public ResponseObj login(HttpServletRequest request)
    {
        if(CommonUtil.isLogin()){
            return new ResponseObj(0,"已登录");
        }
        if(userService.frontLogin(request.getParameter("mobile"),request.getParameter("password"))){
            return new ResponseObj();
        }
        return new ResponseObj(-1,"登录失败");
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseObj register(HttpServletRequest request, @RequestParam(name = "inviteCode") String inviteCode)
    {
        if(CommonUtil.isLogin()){
            return new ResponseObj(-1,"请退出登录");
        }
        User user = userService.register(request.getParameter("username"),request.getParameter("password"),request.getParameter("mobile"),inviteCode);
        return new ResponseObj(0,"请到登录页面进行登录",user);
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
