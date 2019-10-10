package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.MessageInf;

public interface MessageInfMapper {
    int deleteByPrimaryKey(Integer messageId);

    int insert(MessageInf record);

    int insertSelective(MessageInf record);

    MessageInf selectByPrimaryKey(Integer messageId);

    int updateByPrimaryKeySelective(MessageInf record);

    int updateByPrimaryKey(MessageInf record);
}