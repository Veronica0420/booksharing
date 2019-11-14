package com.ecust.sharebook.mapper;


import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.pojo.util.shelf.book;
import com.ecust.sharebook.pojo.util.shelf.myShelf;

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
    List<Map<String, Object>> list1(Map<String, Object> map);


    List<rUserBook>SelectByIsbn(Map<String, Object> map);


    int save(Map<String, Object> map);

    /**
     * shelf 加载
     * **/
    List<myShelf> findShelf(Map<String, Object> map);

    /**
     * othershelf 加载
     * **/
    List<myShelf> findOtherShelf(Map<String, Object> map);


    /**
     * shelf-list 加载
     * **/
    List<myShelf>  findShelfCateLog(Map<String, Object> map);

    /**
     * othershelf-list 加载
     * **/
    List<myShelf>  findOtherShelfCateLog(Map<String, Object> map);



    List<rUserBook> list(Map<String, Object> map);


    /**
     * 查询申请
     * **/
    List<MessageInf> listByState1(Map<String, Object> map);

    /**
     * 查询归还
     * **/
    List<MessageInf> listByState2(Map<String, Object> map);

    List<book> borrowShelf (Map<String, Object> map);

    List<book> ownershelf (Map<String, Object> map);

    List<Map<String , Object>>findOwnerBookList(Map<String, Object> map);

    List<Map<String , Object>>findBorrowBookList(Map<String, Object> map);



}