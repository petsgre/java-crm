package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import static com.example.demo.utils.JwtUtils.privateKey;

@RestController
public class UserStateController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/test"}, method = {RequestMethod.GET})
    public void test() {
//        rBucket.set("xxxxxxxxxx");
//        userinfo.set("asdasdasd");
//        String bucketString=rBucket.get();
//        System.out.println(bucketString);
    }

    @RequestMapping(value = {"/user/login"}, method = {RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> login(@RequestBody String body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String pwd = jsonNode.get("pwd").asText();
        String userId = String.valueOf(jsonNode.get("userId").asText());
        User user = userService.getUserById(userId, pwd);
        if (user != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", user.getName());
            payload.put("address", user.getAddress());
            payload.put("age", user.getAge());
            payload.put("id", user.getId());
            String token = new JwtUtils().createToken(payload);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "你这账户有问题啊！账号密码不对！");
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
    }
}
