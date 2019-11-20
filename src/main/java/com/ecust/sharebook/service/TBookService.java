package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.pojo.MessageInf;

import java.util.List;
import java.util.Map;


public interface TBookService {

    BookInf selectByIsbn(String isbn);

    List<BookInf> list(Map<String, Object> map);

    public List<BookInf> findbyIsbn(Map<String, Object> map);

    BookInf selectByPrimaryKey(String isbn);

    List<BookInf> ScanResult(Map<String, Object> map);

    int deleteByPrimaryKey(String isbn);

    int insert(BookInf record);

    int insertSelective(BookInf record);


    int updateByPrimaryKeySelective(BookInf record);

    int updateByPrimaryKey(BookInf record);

    BookInf fingByMessageId(Map<String,Object> map);



}
