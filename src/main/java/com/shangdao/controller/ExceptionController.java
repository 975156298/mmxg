package com.shangdao.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(java.lang.Exception.class)
    @ResponseBody
    public Map <String,Object> handleBizExp(java.lang.Exception ex) throws java.lang.Exception {
        Map<String ,Object> map=new HashMap<>();
        map.put("statusCode",-1);
        map.put("message",ex.getMessage());
        return map;
    }
}
