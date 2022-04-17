package com.example.demo.entity;

public class User {
    private String name;
    private String id;
    private String address;
    private Integer age;
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", id='" + id + '\'' + ", address='" + address + '\'' + ", age=" + age + ", pwd='" + pwd + '\'' + '}';
    }

    public User() {
    }

    public User(String id, String name, String address, Integer age, String pwd) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
