package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.Admin;

import java.util.Map;

public interface AdminMapper {
    int deleteByPrimaryKey(Integer adminid);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer adminid);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin list(Map<String,Object> map);
}