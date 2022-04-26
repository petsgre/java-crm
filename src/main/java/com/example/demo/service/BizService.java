package com.example.demo.service;

import com.example.demo.entity.Biz;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【t_biz】的数据库操作Service
 * @createDate 2022-04-22 22:08:16
 */
public interface BizService {
    public List<Biz> getList();
}
