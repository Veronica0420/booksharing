package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookInf;

public interface BookInfMapper {
    int deleteByPrimaryKey(String isbn);

    int insert(BookInf record);

    int insertSelective(BookInf record);

    BookInf selectByPrimaryKey(String isbn);

    int updateByPrimaryKeySelective(BookInf record);

    int updateByPrimaryKey(BookInf record);
}