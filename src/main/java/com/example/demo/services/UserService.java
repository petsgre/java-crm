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

    public User getUserById(String id) {
        User user = userDao.getUserById(id);
        return user;
    }

    public User selectUser(String id, String pwd) {
        User user = userDao.selectUser(id, pwd);
        return user;
    }

    public boolean insertUser(User user) {
        userDao.insertUser(user.getId(), user.getPwd(), user.getAge(), user.getAddress(), user.getName());
        return true;
    }

    public boolean deleteUser(String id) {
        userDao.deleteUser(id);
        return true;
    }

    public void updateUser(User user) {
        userDao.updateUser(user.getId(), user.getPwd(), user.getAge(), user.getAddress(), user.getName());
    }
}
