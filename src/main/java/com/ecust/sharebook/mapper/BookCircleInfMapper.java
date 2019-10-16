package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookCircleInf;

import java.util.List;
import java.util.Map;

public interface BookCircleInfMapper {
    int deleteByPrimaryKey(Integer bookCircleId);

    int insert(BookCircleInf record);

    int insertSelective(BookCircleInf record);

    BookCircleInf selectByPrimaryKey(Integer bookCircleId);

    int updateByPrimaryKeySelective(BookCircleInf record);

    int updateByPrimaryKey(BookCircleInf record);

    //根据一定条件查出对应的所有图书圈列表   ——Sijar
    List<BookCircleInf> listBycreaterID(Map<String, Object> map);
}