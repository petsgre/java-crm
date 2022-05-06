package com.example.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = {"/common"}, produces = "application/json;charset=UTF-8")
public class CommonController {
    @GetMapping(value = {"/bg"})
    public ResponseEntity<Map> getBg() {

        HashMap<Object, Object> map = new HashMap<>();
        HashMap<String, String> xxx = new HashMap<String, String>();
        String url = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=1&n=10&mkt=en-US";
        HttpGet get = new HttpGet(url);
        get.addHeader("Content-Type", "application/json");
        HttpClient httpClientBuilder = HttpClientBuilder.create().build();
        try {
            HttpResponse response = httpClientBuilder.execute(get);
            HttpEntity entity = response.getEntity();
            String resBody = EntityUtils.toString(entity);
            JsonNode jsonNode = new ObjectMapper().readTree(resBody);
            map.put("bgInfo", jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
