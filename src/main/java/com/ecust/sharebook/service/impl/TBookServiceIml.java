package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.BookInfMapper;
import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.service.TBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class TBookServiceIml implements TBookService {
    @Autowired
    private BookInfMapper tBookInfMapper;

    @Override
    public BookInf selectByIsbn(String isbn) {
        return tBookInfMapper.selectByPrimaryKey(isbn);
    }
}
