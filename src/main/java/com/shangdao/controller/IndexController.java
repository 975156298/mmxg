package com.shangdao.controller;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Device device)
    {
        if(device.isMobile()){
            return "redirect:/mobile/www/index.html";
        }
        return "index";
    }
}
