package com.shangdao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangdao.domain.config.ConfigLog;
import com.shangdao.domain.config.ConfigLogRepository;
import com.shangdao.domain.reward.RewardRepository;
import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

@Controller
@Profile("dev")
public class TestController {
    @Resource
    ConfigLogRepository configLogRepository;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    EntityManagerFactory entityManagerFactory;

    @GetMapping("/test")
    @ResponseBody
    public Object test(HttpServletRequest request, Pageable pageable) {
        return "0";
    }
}