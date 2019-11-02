package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookInf;

import java.util.List;
import java.util.Map;

public interface BookInfMapper {
    int deleteByPrimaryKey(String isbn);

    int insert(BookInf record);

    int insertSelective(BookInf record);

    BookInf selectByPrimaryKey(String isbn);

    int updateByPrimaryKeySelective(BookInf record);

    int updateByPrimaryKey(BookInf record);

    List<BookInf> list(Map<String, Object> map);

    List<BookInf> findbyIsbn(Map<String, Object> map);
}