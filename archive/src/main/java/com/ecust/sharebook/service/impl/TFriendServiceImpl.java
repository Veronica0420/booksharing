package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.FriendInfMapper;
import com.ecust.sharebook.pojo.FriendInf;
import com.ecust.sharebook.pojo.util.friend.friendList;
import com.ecust.sharebook.service.TFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TFriendServiceImpl  implements TFriendService {

    @Autowired
    private FriendInfMapper friendInfMapper;
    @Override
    public List<friendList> getList(Map<String, Object> map) {
        List<friendList> list = friendInfMapper.getList(map);
        if(list!=null && list.size()!=0){
            return list;
        }
        return  null;
    }

    @Override
    public int insert(FriendInf record) {
        return friendInfMapper.insert(record);
    }

    @Override
    public int insertSelective(FriendInf record) {
        return friendInfMapper.insertSelective(record);
    }
}
