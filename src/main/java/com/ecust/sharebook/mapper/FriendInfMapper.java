package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.FriendInf;

public interface FriendInfMapper {
    int deleteByPrimaryKey(Integer fdriendId);

    int insert(FriendInf record);

    int insertSelective(FriendInf record);

    FriendInf selectByPrimaryKey(Integer fdriendId);

    int updateByPrimaryKeySelective(FriendInf record);

    int updateByPrimaryKey(FriendInf record);
}