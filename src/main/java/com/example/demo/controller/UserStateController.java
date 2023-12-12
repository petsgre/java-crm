package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static com.example.demo.constant.Constant.Origin.Browser;

@RestController
@RequestMapping(value = {"/user"}, produces = "application/json;charset=UTF-8")
public class UserStateController {
    @Autowired
    private UserService userService;

    @Value("${spring.profiles.active}")
    private String active;

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
            map.put("state", "success delete！");
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody String body, HttpServletResponse httpResponse) throws IOException, InterruptedException {
        System.out.println(active);
        System.out.println("****************");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String pwd = jsonNode.get("pwd") != null ? jsonNode.get("pwd").asText() : null;
        String userId = jsonNode.get("userId") != null ? jsonNode.get("userId").asText() : null;
        if (!StringUtils.hasText(pwd) || !StringUtils.hasText(userId)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msg", "请输入正确的账号密码");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.UNAUTHORIZED);
        }
        String origin = jsonNode.get("origin") != null ? jsonNode.get("origin").asText() : String.valueOf(Browser);
        DemoApplication.logger.info(origin);
        User user = userService.selectUser(userId, pwd);
        if (user != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", user.getName());
            payload.put("address", user.getAddress());
            payload.put("age", user.getAge());
            payload.put("id", user.getId());
            String token = new JwtUtils().createToken(payload, origin, active);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("success", true);
            // create a cookie
            // TODO: 2022/5/22
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "你这账户有问题啊！账号密码不对！");
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.UNAUTHORIZED);
    }
}
