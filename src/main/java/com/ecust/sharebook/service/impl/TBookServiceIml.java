package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.BookInfMapper;
import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.service.TBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class TBookServiceIml implements TBookService {
    @Autowired
    private BookInfMapper tBookInfMapper;

    //scan
    @Override
    public BookInf selectByIsbn(String isbn) {
        return tBookInfMapper.selectByPrimaryKey(isbn);
    }

    @Override
    public List<BookInf> list(Map<String, Object> map) {
        List<BookInf> list = tBookInfMapper.list(map);
        if(list!=null && list.size()!=0){
            return  list;
        }
        return  null;
    }


    /**
     * 功能：根据isbn 查询书籍相对类别，一对多关系
     * 注：存在book_catg表中无此isbn 的可能,此时catgId 设为-1
     * **/
    @Override
    public List<BookInf> findbyIsbn(Map<String, Object> map) {
        List<BookInf> list =tBookInfMapper.findbyIsbn(map);

        if (list!=null && list.size()>0){
            System.out.println(list.get(0));
            return  list;
        }
        return  null;
    }


}
