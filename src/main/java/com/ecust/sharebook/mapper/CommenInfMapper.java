package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.CommenInf;

public interface CommenInfMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(CommenInf record);

    int insertSelective(CommenInf record);

    CommenInf selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(CommenInf record);

    int updateByPrimaryKey(CommenInf record);
}