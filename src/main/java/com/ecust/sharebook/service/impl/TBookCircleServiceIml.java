package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.BookCircleInfMapper;
import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.BookCircleInf;
import com.ecust.sharebook.service.TBookCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TBookCircleServiceIml implements TBookCircleService {
    @Autowired
    private BookCircleInfMapper tBookCircleInfMapper;
    @Autowired
    private rBcircleMemberMapper trBcircleMemberMapper;

    @Override
    public int insert(Map<String, Object> bcInf){
        int result=tBookCircleInfMapper.insert(bcInf);
        return result;
    }

    @Override
    public List<BookCircleInf> selectLikBCName(Map<String, Object> param){
        List<BookCircleInf> list=tBookCircleInfMapper.selectLikebcName(param);
        if(list!=null&&list.size()>0){
            return list;
        }
         return null;

    }

    @Override
    public List<BookCircleInf> selectbyCreaterID(Map<String, Object> params){
        List<BookCircleInf > list = tBookCircleInfMapper.listBycreaterID(params);
        List<Integer> list_id = trBcircleMemberMapper.selectByMemberId(params);
        for(int i=0 ;i<list_id.size();i++){
            System.out.println("map.put  list_id"+i+"="+list_id.get(i));
            BookCircleInf tmp=tBookCircleInfMapper.selectByPrimaryKey(list_id.get(i));
            list.add(tmp);
        }
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }

    @Override
    public List<BookCircleInf> selectbyNotCreaterIDMemberID(Map<String, Object> params){
        List<Integer> list_id = trBcircleMemberMapper.selectByMemberId(params);  //本用户不是图书圈成员的所有图书圈ID的list
        Map<String,Object> map=new HashMap<>();
        map.put("list_id",list_id);
        List<BookCircleInf > list = tBookCircleInfMapper.listByNotcreaterID(params);

        for (Iterator<BookCircleInf> iterator = list.iterator(); iterator.hasNext();) {
            BookCircleInf x = iterator.next();
            for (int i = 0; i < list_id.size(); i++) {
                if (list_id.get(i).equals(x.getBookCircleId())) {
                    iterator.remove();
                    continue;
                }
            }
        }
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }
}
