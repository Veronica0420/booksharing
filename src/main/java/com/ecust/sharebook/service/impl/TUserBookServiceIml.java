package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rUserBookMapper;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.service.TUserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TUserBookServiceIml implements TUserBookService {
    @Autowired
    private rUserBookMapper trUserBookMapper;

    @Override
    public   List<String > selectISBNbyID(Map<String, Object> params) {
        List<String > list = trUserBookMapper.list(params);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }
}
