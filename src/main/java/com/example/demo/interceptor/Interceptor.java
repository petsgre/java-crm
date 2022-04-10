package com.example.demo.interceptor;

import com.example.demo.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.HashMap;

@Configuration
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        System.out.println("in interceptor 进入了拦截器！！！！！!!!!!!!!!");
        HashMap<String, Object> resMap = new HashMap<>();
        if (token == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            resMap.put("state", "你未登录！！！");
            String json = new ObjectMapper().writeValueAsString(resMap);
            response.getWriter().println(json);
            return false;
        }
        boolean flag = new JwtUtils().verifyToken(token);
        if (flag) {
            return true;
        } else {
            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            resMap.put("state", "登录态已失效！！！");
            String json = new ObjectMapper().writeValueAsString(resMap);
            response.getWriter().println(json);
            return false;
        }
    }


}
