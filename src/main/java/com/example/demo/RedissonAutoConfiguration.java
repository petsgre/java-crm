package com.example.demo;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

//@Configuration
public class RedissonAutoConfiguration {

//    @Bean
//    public RedissonClient redisson(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
////                .setConnectTimeout(timeout) // 未设置使用默认
////                .setDatabase(redisProperties.getDatabase()) // 未设置使用默认
////                .setPassword(redisProperties.getPassword()); // 未设置使用默认
////        RedissonClient redissonClient = Redisson.create(config);
//        return Redisson.create(config);
//    }
}
