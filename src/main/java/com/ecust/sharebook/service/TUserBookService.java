package com.ecust.sharebook.service;


import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.pojo.util.shelf.myShelf;

import java.util.List;
import java.util.Map;

public interface TUserBookService {

    List<Map<String, Object>>  selectByOwId(Map<String, Object> params);

    rUserBook SelectByIsbn(Map<String, Object> map);


    /**
     * 部分更新  by bookId
     * **/
    int updateByPrimaryKeySelective(rUserBook record);

    /**
     * 删除  by bookId
     * **/

    int deleteByPrimaryKey(Integer bookId);

    int save(Map<String, Object> map);

    rUserBook selectByPrimaryKey(Integer bookId);

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
    List<myShelf> findShelfCateLog(Map<String, Object> map);

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


}
