package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.interceptor.Interceptor;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
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
    @Autowired
    private AsyncClass asyncClass;

    @Autowired
    @Lazy
    HelloWorldController helloWorldController;

    @Async
    public void asyncTask() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("asyncTask");
        DemoApplication.logger.info("123");
    }

    @RequestMapping(value = {"/test"}, method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String index() throws InterruptedException {

        System.out.println(System.currentTimeMillis());
        long l1 = System.currentTimeMillis();
        asyncClass.task1();
        helloWorldController.asyncTask();
//        this.task1();
//        this.task2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 模拟耗时操作
                    Thread.sleep(3000);
                    System.out.println("111111111111111");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1);
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
        strings.add("/user/login");
        strings.add("/test");
        strings.add("/error");
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**").excludePathPatterns(strings);
        super.addInterceptors(registry);
    }
}
