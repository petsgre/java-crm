package com.example.demo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Map;

import static com.example.demo.DemoApplication.kkk;

public class JwtUtils {
    public static final String privateKey = "askldjsdklfjasldkjlkasdjklasjdlkasjdlkjasdlkjaskld";

    public String createToken(Map<String, Object> map) {
        if (kkk == null) {
            System.out.println("open redis get key");
            Config config = new Config();
            config.useSingleServer().setAddress("redis://127.0.0.1:6379");
            RedissonClient redisson = Redisson.create(config);
            RBucket<String> redisKey = redisson.getBucket("privateKey");
            String remoteKey = redisKey.get();
            redisson.shutdown();
            if (remoteKey != null) {
                kkk = remoteKey;
            } else {
                redisKey.set(privateKey);
                kkk = privateKey;
            }
        }
        System.out.println(kkk);
        JwtBuilder jwtBuilder = Jwts.builder();
        // 添加payload
        map.forEach((k, value) -> {
            jwtBuilder.claim(k, value);
        });
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3600);
        // 添加过期时间
        jwtBuilder.setExpiration(calendar.getTime());

        // 生成jwt string
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(kkk == null ? privateKey : kkk));
        return jwtBuilder.signWith(key).compact();
    }

    public boolean verifyToken(String token) {
        Jws<Claims> jws = null;
        try {
            String privateKeyStr = this.getPrivateKeyFromRedis();
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKeyStr == null ? privateKey :
                    privateKeyStr));
            jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println(jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
        return jws != null;
    }

    public String getPrivateKeyFromRedis() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RBucket<String> redisKey = redisson.getBucket("privateKey");
        String remoteKey = redisKey.get();
        redisson.shutdown();
        return remoteKey;
    }
}