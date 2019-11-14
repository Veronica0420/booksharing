package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.FriendInf;

import java.util.List;
import java.util.Map;

public interface TFriendService {

    int insert(FriendInf record);

    int insertSelective(FriendInf record);

    int deleteByPrimaryKey(Integer fdriendId);

    FriendInf selectByPrimaryKey(Integer fdriendId);

    int updateByPrimaryKeySelective(FriendInf record);

    int updateByPrimaryKey(FriendInf record);

    List<FriendInf> getList (Map<String, Object> map);

    List<FriendInf> list(Map<String, Object> map);

    List<FriendInf> list2(Map<String, Object> map);

    List<FriendInf> list3(Map<String, Object> map);

    Map<String ,Object> friendInfo(Map<String, Object> map);

    List<FriendInf> messageList (Map<String, Object> map);

    List<FriendInf> messageAllList (Map<String, Object> map);







}
