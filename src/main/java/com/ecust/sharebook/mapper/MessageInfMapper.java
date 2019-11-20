package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.MessageInf;

import java.util.List;
import java.util.Map;

public interface MessageInfMapper {
    int deleteByPrimaryKey(Integer messageId);

    int insert(MessageInf record);

    int insertSelective(MessageInf record);

    MessageInf selectByPrimaryKey(Integer messageId);

    int updateByPrimaryKeySelective(MessageInf record);

    int updateByPrimaryKey(MessageInf record);


    List<MessageInf> list(Map<String, Object> map);

    List<MessageInf> listAll(Map<String,Object> map);

    List<MessageInf> findCurrentBMessage(Map<String,Object> map);

    List<MessageInf> findHistory(Map<String,Object> map);


    int applyCancelOther(Map<String,Object> map);

    int cancelReturn(Map<String,Object> map);

    int passReturn(Map<String,Object> map);

    int rejectApply(Map<String,Object> map);

    int passApply(Map<String,Object> map);


    int cancelApply(Map<String,Object> map);


    int emptyCountInsertM(Map<String,Object> map);
    int emptyCountInsertB(Map<String,Object> map);






}