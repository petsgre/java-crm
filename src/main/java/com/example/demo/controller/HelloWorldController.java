package com.example.demo.controller;

import com.example.demo.interceptor.Interceptor;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloWorldController extends WebMvcConfigurationSupport {
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/"}, method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String index() {
        ObjectMapper objectMapper = new ObjectMapper();
        List list = userService.getUserList();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        List<String> strings = new ArrayList<String>();
        strings.add("/user/**");
        strings.add("/error");
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**").excludePathPatterns(strings);
        super.addInterceptors(registry);
    }
}
