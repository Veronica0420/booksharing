package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rBookCategoryMapper;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rBookCategory;
import com.ecust.sharebook.service.TBookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TBookCategoryServiceIml implements TBookCategoryService {

    @Autowired
    private rBookCategoryMapper  tBookCategoryMapper;
    @Override
    public rBookCategory findCatgbyIsbn(Map<String, Object> map) {

        List<rBookCategory> list =tBookCategoryMapper.findCatgbyIsbn(map);

        if (list!=null && list.size()>0){
            System.out.println(list.get(0));
            return  list.get(0);
        }
        return  null;
    }

    @Override
    public List<rBookCategory> findbyIsbn(Map<String, Object> map) {
        List<rBookCategory> list =tBookCategoryMapper.findbyIsbn(map);

        if (list!=null && list.size()>0){
            System.out.println(list.get(0));
            return  list;
        }
        return  null;
    }

}
