package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.PostInf;

import java.util.List;
import java.util.Map;

public interface PostInfMapper {
    int deleteByPrimaryKey(Integer postId);

    int insert(PostInf record);

    int insertSelective(PostInf record);

    PostInf selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(PostInf record);

    int updateByPrimaryKey(PostInf record);

    List<PostInf> list(Map<String, Object> map);

    List<PostInf> Plist(Map<String, Object> map);

    PostInf post(Map<String, Object> map);


}