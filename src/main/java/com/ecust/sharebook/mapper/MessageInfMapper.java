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
}