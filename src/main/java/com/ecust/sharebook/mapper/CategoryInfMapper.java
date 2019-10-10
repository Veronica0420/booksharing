package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.CategoryInf;

import java.util.List;
import java.util.Map;

public interface CategoryInfMapper {
    int deleteByPrimaryKey(Integer catgId);

    int insert(CategoryInf record);

    int insertSelective(CategoryInf record);

    CategoryInf selectByPrimaryKey(Integer catgId);

    int updateByPrimaryKeySelective(CategoryInf record);

    int updateByPrimaryKey(CategoryInf record);

    //自定义
    CategoryInf get(Long id);

    List<CategoryInf> list();
}