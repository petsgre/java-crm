package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/user"}, produces = "application/json;charset=UTF-8")
public class UserStateController {
    @Autowired
    private UserService userService;

    @GetMapping(value = {""})
    public String getUserList() {
        ObjectMapper objectMapper = new ObjectMapper();
        List list = userService.getUserList();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") String id) {
        HashMap<String, String> map = new HashMap<>();
        try {
            userService.deleteUser(id);
            map.put("state", "success delete");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "error");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> addUser(@RequestBody Map<String, Object> body) {
        User user = new ObjectMapper().convertValue(body.get("user"), User.class);
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        HashMap<String, String> map = new HashMap<>();
        try {
            userService.insertUser(user);
            map.put("state", "success insert");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "error");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody Map<String, Object> body) {
        User user = new ObjectMapper().convertValue(body.get("user"), User.class);
        HashMap<String, String> map = new HashMap<>();
        try {
            userService.updateUser(user);
            map.put("state", "success update");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "error");
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> login(@RequestBody String body) throws IOException,
            InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String pwd = jsonNode.get("pwd").asText();
        String userId = String.valueOf(jsonNode.get("userId").asText());
        User user = userService.selectUser(userId, pwd);
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
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.UNAUTHORIZED);
    }
}
