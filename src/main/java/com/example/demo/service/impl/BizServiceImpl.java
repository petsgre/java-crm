package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.Biz;
import com.example.demo.mapper.BizMapper;
import com.example.demo.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【t_biz】的数据库操作Service实现
 * @createDate 2022-04-22 22:08:16
 */
@Service
public class BizServiceImpl implements BizService {

    @Autowired
    public BizMapper bizMapper;

    @Override
    public List<Biz> getList() {
        List list = bizMapper.getList();
        return list;
    }
}
