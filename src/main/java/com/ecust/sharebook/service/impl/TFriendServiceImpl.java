package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.FriendInfMapper;
import com.ecust.sharebook.pojo.FriendInf;
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
    public List<FriendInf> getList(Map<String, Object> map) {
        List<FriendInf> list = friendInfMapper.getList(map);
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

    @Override
    public int deleteByPrimaryKey(Integer fdriendId) {
        return friendInfMapper.deleteByPrimaryKey(fdriendId);
    }

    @Override
    public FriendInf selectByPrimaryKey(Integer fdriendId) {
        return friendInfMapper.selectByPrimaryKey(fdriendId);
    }

    @Override
    public int updateByPrimaryKeySelective(FriendInf record) {
        return friendInfMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FriendInf record) {
        return friendInfMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<FriendInf> list(Map<String, Object> map) {
        List<FriendInf> list = friendInfMapper.list(map);
        if(list!=null && list.size()!=0){
            return list;
        }
        return  null;
    }

    @Override
    public List<FriendInf> list2(Map<String, Object> map) {
        List<FriendInf> list = friendInfMapper.list2(map);
        if(list!=null && list.size()!=0){
            return list;
        }
        return  null;
    }

    @Override
    public List<FriendInf> list3(Map<String, Object> map) {
        List<FriendInf> list = friendInfMapper.list3(map);
        if(list!=null && list.size()!=0){
            return list;
        }
        return  null;
    }

    @Override
    public Map<String, Object> friendInfo(Map<String, Object> map) {
        return friendInfMapper.friendInfo(map).get(0);
    }

    @Override
    public List<FriendInf> messageList(Map<String, Object> map) {
        return friendInfMapper.messageList(map);
    }

    @Override
    public List<FriendInf> messageAllList(Map<String, Object> map) {
        return friendInfMapper.messageAllList(map);
    }
}
