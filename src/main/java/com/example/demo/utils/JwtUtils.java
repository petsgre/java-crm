package com.example.demo.utils;

import cn.hutool.core.util.StrUtil;
import com.example.demo.DemoApplication;
import com.example.demo.constant.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.demo.constant.Constant.redisAddress;


public class JwtUtils {
    public static final String privateKey = "askldjsdklfjasldkjlkasdjklasjdlkasjdlkjasdlkjaskld";

    @Async
    public void writeToRedis(String userId, String privateKey, String token, String active) throws InterruptedException,
            JsonProcessingException {
        Config config = new Config();
        String address;
        if ("dev".equals(active)) {
            address = "redis://127.0.0.1:6379";
        } else {
            address = "redis://redishostname:6379";
        }
        System.out.println(active);
        config.useSingleServer().setAddress(address);
        RedissonClient redisson = Redisson.create(config);
        RBucket<String> bucket = redisson.getBucket(userId);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("privateKey", privateKey);
        userInfo.put("token", token);
        bucket.set(new ObjectMapper().writeValueAsString(userInfo));
        redisson.shutdown();
    }

    public String getExistToken(String userId) {
        JsonNode userInfo = null;
        try {
            userInfo = this.getUserInfoFromRedis(userId);
        } catch (JsonProcessingException e) {
            return null;
        }
        String privateKeyStr = userInfo.get("privateKey").toString();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKeyStr));
        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder.signWith(key).compact();
        return token;
    }

    public String createToken(Map<String, Object> payload, String origin, String active) throws InterruptedException,
            JsonProcessingException {
        System.out.println("open redis get key*******************************");
        String userId = payload.get("id").toString();
        if (Constant.Origin.MiniApp.name().equalsIgnoreCase(origin)) {
            userId = Constant.Origin.MiniApp.name() + userId;
        }
        String privateKeyStr = privateKey + String.valueOf(new Random().nextFloat()).split("\\.")[1];
        System.out.println(privateKeyStr);
//        String token = this.getExistToken(userId);
//        if (StringUtils.hasText(token)) {
//            return token;
//        }

        JwtBuilder jwtBuilder = Jwts.builder();
        // 添加payload
        payload.forEach((k, value) -> {
            jwtBuilder.claim(k, value);
        });
        if (Constant.Origin.MiniApp.name().equalsIgnoreCase(origin)) {
            jwtBuilder.claim("id", userId);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3600);
        // 添加过期时间
        jwtBuilder.setExpiration(calendar.getTime());
        // 生成jwt string
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKeyStr));
        String token = jwtBuilder.signWith(key).compact();
        this.writeToRedis(userId, privateKeyStr, token, active);

        return token;
    }

    public boolean verifyToken(String privateKeyStr, String token) {
        Jws<Claims> jws = null;
        if (StrUtil.hasBlank(privateKeyStr)) {
            return false;
        }
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKeyStr));
        jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        System.out.println(jws);

        return jws != null;
    }

    public JsonNode getUserInfoFromRedis(String userId) throws JsonProcessingException {
        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress);
        RedissonClient redisson = Redisson.create(config);
        RBucket<String> bucket = redisson.getBucket(userId);
        String userInfoStr = bucket.get();
        redisson.shutdown();
        if (userInfoStr == null) {
            return null;
        }
        JsonNode userInfoNode = new ObjectMapper().readTree(userInfoStr);
        return userInfoNode;
    }
}
