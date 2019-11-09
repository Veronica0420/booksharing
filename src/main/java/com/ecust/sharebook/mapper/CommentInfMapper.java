package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.CommentInf;

import java.util.List;
import java.util.Map;

public interface CommentInfMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(CommentInf record);

    int insertSelective(CommentInf record);

    CommentInf selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(CommentInf record);

    int updateByPrimaryKey(CommentInf record);


    List<CommentInf> list(Map<String, Object> map);
}