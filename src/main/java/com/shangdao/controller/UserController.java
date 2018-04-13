package com.shangdao.controller;

import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import com.shangdao.service.UserService;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.RegexUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/front/setSecondPassword")
    public ResponseObj secondPassword(HttpServletRequest request) {
        userService.setSecondPassword(request.getParameter("secondPassword"));
        return new ResponseObj();
    }


    @GetMapping("/front/user/self")
    public ResponseObj self() {
        return new ResponseObj(userRepository.findUserByMobile(CommonUtil.getPrincipal().getMobile()));
    }

    @GetMapping("/admin/user/list")
    public Page<User> list(Pageable pageable,@RequestParam(value = "mobile",required = false) String mobile) {
        if(mobile!=null){
            return userRepository.findAllByMobile(mobile,pageable);
        }
        return userRepository.findAll(pageable);
    }

    @GetMapping("/front/user/findWalletAddressByMobile")//根据手机号搜索地址
    public ResponseObj findWalletAddressByMobile(HttpServletRequest request) {
        if (!RegexUtil.isMobile(request.getParameter("mobile"))) {
            return new ResponseObj();
        }

        User user= userRepository.findUserByMobile(request.getParameter("mobile"));
        if(user==null){
            return new ResponseObj();
        }

        return new ResponseObj(user.getWalletAddress());
    }

    @PostMapping("/front/user/setAvatar")
    public ResponseObj avatar(HttpServletRequest request)
    {
        userService.setAvatar(request.getParameter("avatar"));
        return new ResponseObj();
    }

    @PostMapping("/admin/user/resetpwd")
    public ResponseObj resetpwd(@RequestParam(name = "user_id") Integer userId){
        userService.resetPassword(userId);
        return new ResponseObj();
    }


    @PostMapping("/front/user/updatepwd")
    public ResponseObj updatePWD(@RequestParam(name = "oldPassword") String oldPassword,@RequestParam(name = "newPassword") String newPassword){
        userService.updatePassword(oldPassword,newPassword);
        return new ResponseObj();
    }
}
