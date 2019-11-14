package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.PostInf;

import java.util.List;
import java.util.Map;

public interface TPostInfService {


    List<PostInf> list(Map<String, Object> map);

    List<PostInf> Plist(Map<String, Object> map);

    PostInf post(Map<String, Object> map);


    int deleteByPrimaryKey(Integer postId);

    int insert(PostInf record);

    int insertSelective(PostInf record);

    PostInf selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(PostInf record);

    int updateByPrimaryKey(PostInf record);



}
