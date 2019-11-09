package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.rBcircleMember;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.service.TBcircleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TBcircleMemberServiceImpl  implements TBcircleMemberService {
    @Autowired
    private rBcircleMemberMapper tBcircleMemberMapper;


    @Override
    public List<Map<String, Object>> findShelf(Map<String, Object> param) {
        return tBcircleMemberMapper.findShelf(param);
    }

    @Override
    public List<Map<String, Object>> findCatgShelf(Map<String, Object> param) {
        return tBcircleMemberMapper.findCatgShelf(param);
    }

    @Override
    public List<circleCatgBook> findCatg(List<Map<String, Object>> list) {
        return tBcircleMemberMapper.findCatg(list);
    }

    @Override
    public List<rBcircleMember> list(Map<String, Object> map) {
        List<rBcircleMember> lists = tBcircleMemberMapper.list(map);
        if(lists.size()!=0 && lists!=null){
            return  lists;
        }
        return null;
    }

    @Override
    public int insert(rBcircleMember record) {
        return tBcircleMemberMapper.insert(record);
    }

    @Override
    public int insertmap(Map<String, Object> map) {
        return tBcircleMemberMapper.insertmap(map);
    }

    @Override
    public int insertSelective(rBcircleMember record) {
        return tBcircleMemberMapper.insertSelective(record);
    }


}
