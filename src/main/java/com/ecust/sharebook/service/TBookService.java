package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.BookInf;

import java.util.List;
import java.util.Map;


public interface TBookService {

    BookInf selectByIsbn(String isbn);

    List<BookInf> list(Map<String, Object> map);

    public List<BookInf> findbyIsbn(Map<String, Object> map);


}
