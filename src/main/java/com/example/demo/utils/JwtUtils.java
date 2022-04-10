package com.example.demo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    public static final String privateKey = "askldjsdklfjasldkjlkasdjklasjdlkasjdlkjasdlkjaskld";

    public String createToken(Map<String, Object> map) {
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
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        return jwtBuilder.signWith(key).compact();
    }

    public boolean verifyToken(String token) {
        Jws<Claims> jws = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println(jws);
        } catch (JwtException ex) {
            ex.printStackTrace();
        }
        return jws != null;
    }
}