package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.PostInf;

public interface PostInfMapper {
    int deleteByPrimaryKey(Integer postId);

    int insert(PostInf record);

    int insertSelective(PostInf record);

    PostInf selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(PostInf record);

    int updateByPrimaryKey(PostInf record);
}