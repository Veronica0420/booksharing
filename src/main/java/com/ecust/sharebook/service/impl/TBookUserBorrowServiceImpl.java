package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rBookUserBorrowMapper;
import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.service.TBookUserBorrowService;
import com.ecust.sharebook.service.TMessageService;
import com.ecust.sharebook.service.TUserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TBookUserBorrowServiceImpl implements TBookUserBorrowService {

    @Autowired
    private rBookUserBorrowMapper tBookUserBorrowMapper;
    @Autowired
    private TUserBookService tUserBookService;
    @Autowired
    private TMessageService tMessageService;

    @Override
    public  List<Map<String, Object>>listByUser(Map<String, Object> map) {

        List<Map<String, Object>> list=tBookUserBorrowMapper.listByUser(map);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;

    }

    @Override
    public List<rBookUserBorrow> list(Map<String, Object> map) {
        List<rBookUserBorrow> list=tBookUserBorrowMapper.list(map);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }

    @Override
    public rBookUserBorrow SelectByIsbn(Map<String, Object> map) {
        List<rBookUserBorrow> list=tBookUserBorrowMapper.SelectByIsbn(map);
        if (list!=null && list.size()>0){
            return  list.get(0);
        }
        return null;
    }

    @Override
    public int updateState(Map<String, Object> map) {
        return tBookUserBorrowMapper.updateState(map);
    }

    @Override
    public int updateByPrimaryKeySelective(rBookUserBorrow record) {
        return tBookUserBorrowMapper.updateByPrimaryKeySelective(record);
    }

    @Transactional
    @Override
    public int save(rBookUserBorrow rBookUserBorrows, rUserBook record, MessageInf message) {

        Map<String,Integer> map = new HashMap<>();


        Integer borrowId = tBookUserBorrowMapper.insertSelective(rBookUserBorrows);

        //更新 rUserBook 状态为待处理
        int j = tUserBookService.updateByPrimaryKeySelective(record);

        int result = 0;
        //创建Message type = 0（申请）
        if(borrowId>0 && j ==1){
            message.setmBorrowId(borrowId);
            int  m= tMessageService.insertSelective(message);
            result = 1;
        }else
            result = 0;



        return  result;
    }

    @Override
    public List<rBookUserBorrow> listByState(Map<String, Object> map) {
        List<rBookUserBorrow> list=tBookUserBorrowMapper.listByState(map);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }


}
