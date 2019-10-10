package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.AgreeInf;

public interface AgreeInfMapper {
    int deleteByPrimaryKey(Integer agreeId);

    int insert(AgreeInf record);

    int insertSelective(AgreeInf record);

    AgreeInf selectByPrimaryKey(Integer agreeId);

    int updateByPrimaryKeySelective(AgreeInf record);

    int updateByPrimaryKey(AgreeInf record);
}