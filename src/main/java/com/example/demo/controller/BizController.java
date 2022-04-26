package com.example.demo.controller;

import com.example.demo.service.BizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/biz"}, produces = "application/json;charset=UTF-8")
public class BizController {

    @Autowired
    public BizService bizService;

    @GetMapping()
    public String getBizList() {
        ObjectMapper objectMapper = new ObjectMapper();
        List list = bizService.getList();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
