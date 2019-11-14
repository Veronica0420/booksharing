package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.rBcircleMember;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.pojo.util.shelf.book;
import com.ecust.sharebook.service.TBcircleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TBcircleMemberServiceImpl implements TBcircleMemberService {
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
        if (lists.size() != 0 && lists != null) {
            return lists;
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

    @Override
    public List<book> findShelfCircle(Map<String, Object> map) {
        return tBcircleMemberMapper.findShelfCircle(map);
    }

    @Override
    public List<Map<String, Object>> findCircleBookList(Map<String, Object> map) {
        return tBcircleMemberMapper.findCircleBookList(map);
    }

    @Override
    public int deleteByPrimaryKey(Integer bcMemId) {
        return 0;
    }

    @Override
    public rBcircleMember selectByPrimaryKey(Integer bcMemId) {
        return tBcircleMemberMapper.selectByPrimaryKey(bcMemId);
    }

    @Override
    public List<Integer> selectByMemberId(Map<String, Object> map) {
        return tBcircleMemberMapper.selectByMemberId(map);
    }

    @Override
    public int updateByPrimaryKeySelective(rBcircleMember record) {
        return tBcircleMemberMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(rBcircleMember record) {
        return tBcircleMemberMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deletelist(List<Integer> list) {

        int i = 0;
        System.out.println("size" + list.size());
        for (i =0; i < list.size(); i++) {

                Integer bcMemId = list.get(i);

                int j = tBcircleMemberMapper.deleteByPrimaryKey(bcMemId);

                if (j == 1) {

                } else {

                    break;
                }
        }
        System.out.println();

        if (i != list.size()) {

            return 0;
        } else {

            return 1;
        }
    }

    @Override
    public int delete(Map<String, Object> map) {
        return tBcircleMemberMapper.delete(map);
    }


}
