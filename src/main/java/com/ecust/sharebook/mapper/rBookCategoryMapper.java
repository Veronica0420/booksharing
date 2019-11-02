package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rBookCategory;

import java.util.List;
import java.util.Map;

public interface rBookCategoryMapper {
    int deleteByPrimaryKey(Integer bCatgId);

    int insert(rBookCategory record);

    int insertSelective(rBookCategory record);

    rBookCategory selectByPrimaryKey(Integer bCatgId);

    int updateByPrimaryKeySelective(rBookCategory record);

    int updateByPrimaryKey(rBookCategory record);

    //自定义
    List<rBookCategory>  findCatgbyIsbn(Map<String, Object> map);


    List<rBookCategory> list(Map<String, Object> map);


}