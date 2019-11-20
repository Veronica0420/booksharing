package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;

import java.util.List;
import java.util.Map;

public interface rBookUserBorrowMapper {


    int insert(rBookUserBorrow record);

    int insertSelective(rBookUserBorrow record);

    rBookUserBorrow selectByPrimaryKey(Integer borrowId);

    int updateByPrimaryKeySelective(rBookUserBorrow record);

    int updateByPrimaryKey(rBookUserBorrow record);

    int deleteByPrimaryKey(Integer borrowId);


    //自定义

    List<Map<String, Object>> listByUser(Map<String, Object> map);

    List<rBookUserBorrow> list1(Map<String, Object> map);

    List<rBookUserBorrow>SelectByIsbn(Map<String, Object> map);

    int updateState(Map<String, Object> map);

    List<rBookUserBorrow> list(Map<String, Object> map);

    rBookUserBorrow listCurrent(Map<String, Object> map);

    int save(rBookUserBorrow rBookUserBorrows);

    List<rBookUserBorrow> listByState(Map<String, Object> map);


    int returnopt(Map<String, Object> map);

    Integer  emptyCountM(Map<String, Object> map);

    Integer  emptyCountB(Map<String, Object> map);

    rBookUserBorrow selectByMessageId(Map<String,Object> map);
}


