package com.example.demo.test;

import com.example.demo.DemoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringbootRedissonApplicationTests {
    private String name = "1";

    private void test() {
        System.out.println(this.name);
    }
    public static void main(String[] args) {
        System.out.println("中文位置");
        DemoApplication.logger.info("代码位置.");
    }
}
