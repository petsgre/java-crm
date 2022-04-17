package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    public List<User> getUserList();

    public User getUserById(@Param("id") String id);
    public User selectUser(@Param("id") String id, String pwd);

    public boolean insertUser(String id, String pwd, Integer age, String address, String name);

    public boolean deleteUser(String id);

    void updateUser(String id, String pwd, Integer age, String address, String name);
}
