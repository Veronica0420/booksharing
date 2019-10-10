package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rUserBook;

import java.util.List;
import java.util.Map;

public interface rUserBookMapper {
    int deleteByPrimaryKey(Integer bookId);

    int insert(rUserBook record);

    int insertSelective(rUserBook record);

    rUserBook selectByPrimaryKey(Integer bookId);

    int updateByPrimaryKeySelective(rUserBook record);

    int updateByPrimaryKey(rUserBook record);

    //自定义
    List<String> list(Map<String, Object> map);
}