package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.UserInf;

import java.util.List;
import java.util.Map;

public interface UserInfMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInf record);

    int insertSelective(UserInf record);

    UserInf selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInf record);

    int updateByPrimaryKey(UserInf record);

    //自定义

    UserInf selectUserByOepnid(Map<String, Object> map);

    UserInf get(int id);

    int save(UserInf tMember);

    int updateSkeyByOpid(Map<String, Object> map);

    List<UserInf> list(Map<String, Object> map);
}