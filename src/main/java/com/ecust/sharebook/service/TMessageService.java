package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TMessageService {

    List<MessageInf> list(Map<String, Object> map);


    int insertSelective(MessageInf record);

    int updateByPrimaryKeySelective(MessageInf record);

    int deleteByPrimaryKey(Integer messageId);

    int insert(MessageInf record);

    MessageInf selectByPrimaryKey(Integer messageId);


    int updateByPrimaryKey(MessageInf record);

    List<MessageInf> listAll(Map<String,Object> map);


    List<MessageInf> findCurrentBMessage(Map<String,Object> map);

    List<MessageInf> findHistory(Map<String,Object> map);




    int applyCancelOther(Map<String,Object> map);

    int cancelReturn(Map<String,Object> map);

    int passReturn(Map<String,Object> map);

    int rejectApply(Map<String,Object> map);

    int passApply(Map<String,Object> map);

    int cancelApply(Map<String,Object> map);

    int emptyCountInsertB(Map<String,Object> map);

    int emptyCountInsertM(Map<String,Object> map);





}
