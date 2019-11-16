package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.FriendInf;
import com.ecust.sharebook.pojo.util.friend.friendList;

import java.util.List;
import java.util.Map;

public interface FriendInfMapper {
    int deleteByPrimaryKey(Integer fdriendId);

    int insert(FriendInf record);

    int insertSelective(FriendInf record);

    FriendInf selectByPrimaryKey(Integer fdriendId);

    int updateByPrimaryKeySelective(FriendInf record);

    int updateByPrimaryKey(FriendInf record);

    List<friendList> getList (Map<String, Object> map);
}