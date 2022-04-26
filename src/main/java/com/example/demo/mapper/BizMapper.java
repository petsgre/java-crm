package com.example.demo.mapper;

import com.example.demo.entity.Biz;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【t_biz】的数据库操作Mapper
 * @createDate 2022-04-22 22:08:16
 * @Entity com.example.demo.Biz
 */
@Mapper
public interface BizMapper {
    public List<Biz> getList();
}
