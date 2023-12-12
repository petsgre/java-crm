package com.example.demo.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class AsyncClass {
    @Async
    public void task1() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("task1");
    }
}
