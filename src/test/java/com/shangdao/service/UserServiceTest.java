package com.shangdao.service;

import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void frontLogin() {

        boolean result = userService.frontLogin("12300000001","abc135462");
        assertTrue(result);;
    }

    @Test
    public void register() {
        User user = userService.register("test22","123456","12345678901",null);
        assertNotNull(user);
    }

    @Test
    public void setSecondPassword() {
    }
    @Test
    public void temp()
    {
        User user= userRepository.findUserByMobile("12300000001");

        System.out.println("dd");
    }
}