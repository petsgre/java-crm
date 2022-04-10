package com.example.demo.services;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserDao userDao;

    public List<User> getUserList() {
        List list = userDao.getUserList();
        return list;
    }

    public User getUserById(String id, String pwd) {
        User user = userDao.getUserById(id, pwd);
        return user;
    }
}
