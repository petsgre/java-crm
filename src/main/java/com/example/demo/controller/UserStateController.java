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
public class UserStateController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user"}, method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
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

    @RequestMapping(value = {"/user/{id}"}, method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = {"/user"}, method = {RequestMethod.POST})
    public ResponseEntity<Map<String, String>> addUser(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String uuid = UUID.randomUUID().toString();
        User user = new User(uuid, jsonNode.get("name").asText(), jsonNode.get("address").asText(), jsonNode.get("age"
        ).asInt(), jsonNode.get("pwd").asText());
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

    @RequestMapping(value = {"/user"}, method = {RequestMethod.DELETE})
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String id = jsonNode.get("id").asText();
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

    @RequestMapping(value = {"/user"}, method = {RequestMethod.PUT})
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String id = jsonNode.get("id").asText();
        User user = new User(id, jsonNode.get("name") != null ? jsonNode.get("name").asText() : null, jsonNode.get(
                "address") != null ? jsonNode.get("address").asText() : null, jsonNode.get("age") != null ?
                jsonNode.get("age").asInt() : null, jsonNode.get("pwd") != null ? jsonNode.get("pwd").asText() : null);
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
