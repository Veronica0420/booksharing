package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.BookCircleInfMapper;
import com.ecust.sharebook.pojo.BookCircleInf;
import com.ecust.sharebook.service.TBookCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TBookCircleServiceIml implements TBookCircleService {
    @Autowired
    private BookCircleInfMapper tBookCircleInfMapper;

    @Override
    public List<BookCircleInf> selectbyCreaterID(Map<String, Object> params){
        List<BookCircleInf > list = tBookCircleInfMapper.listBycreaterID(params);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }
}
