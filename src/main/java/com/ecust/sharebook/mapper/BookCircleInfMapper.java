package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookCircleInf;

public interface BookCircleInfMapper {
    int deleteByPrimaryKey(Integer bookCircleId);

    int insert(BookCircleInf record);

    int insertSelective(BookCircleInf record);

    BookCircleInf selectByPrimaryKey(Integer bookCircleId);

    int updateByPrimaryKeySelective(BookCircleInf record);

    int updateByPrimaryKey(BookCircleInf record);
}