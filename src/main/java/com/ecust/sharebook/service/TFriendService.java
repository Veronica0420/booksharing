package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.FriendInf;
import com.ecust.sharebook.pojo.util.friend.friendList;

import java.util.List;
import java.util.Map;

public interface TFriendService {

    List<friendList> getList (Map<String, Object> map);

    int insert(FriendInf record);

    int insertSelective(FriendInf record);

}
