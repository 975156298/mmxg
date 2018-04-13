package com.shangdao.controller;

import com.shangdao.domain.config.Config;
import com.shangdao.domain.config.ConfigRepository;
import com.shangdao.service.ConfigService;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ConfigController {

    @Autowired
    ConfigRepository configRepository;
    @Autowired
    ConfigService configService;
    @GetMapping("/front/config/list")
    public List<Config> list()
    {
        return configRepository.findAll();
    }

    @PostMapping("/admin/config/createOrUpdate")//添加更改配置项
    public ResponseObj createOrUpdate(HttpServletRequest request)
    {
        Config config = configService.createOrUpdate(request.getParameter("key"),request.getParameter("value"));

        return new ResponseObj(0,"success",config);
    }

}
