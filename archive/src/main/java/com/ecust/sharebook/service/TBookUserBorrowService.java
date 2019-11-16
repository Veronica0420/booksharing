package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;

import java.util.List;
import java.util.Map;

public interface TBookUserBorrowService {

    List<Map<String, Object>>listByUser(Map<String, Object> map);

    List<rBookUserBorrow> list(Map<String, Object> map);

    rBookUserBorrow SelectByIsbn(Map<String, Object> map);

    int updateState(Map<String, Object> map);

    int updateByPrimaryKeySelective(rBookUserBorrow record);

    List<rBookUserBorrow> listByState(Map<String, Object> map);

    int insertSelective(rBookUserBorrow record);

    rBookUserBorrow selectByPrimaryKey(Integer borrowId);



}
