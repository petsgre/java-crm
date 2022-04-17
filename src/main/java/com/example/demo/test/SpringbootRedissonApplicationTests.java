package com.example.demo.test;

import com.example.demo.DemoApplication;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import java.util.Random;

public class SpringbootRedissonApplicationTests {
    public static void main(String[] args) {
        String xx = String.valueOf(new Random().nextFloat()).split("\\.")[1];
        System.out.println(xx);
    }
}
