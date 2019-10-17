package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rUserBookMapper;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.service.TUserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TUserBookServiceIml implements TUserBookService {
    @Autowired
    private rUserBookMapper trUserBookMapper;

    @Override
    public    List<Map<String, Object>> selectByOwId(Map<String, Object> params) {
        List<Map<String, Object>>  list = trUserBookMapper.list(params);
        if (list!=null && list.size()>0){
            return  list;
        }
        return null;
    }

    @Override
    public rUserBook SelectByIsbn(Map<String, Object> map) {
        List<rUserBook> list=trUserBookMapper.SelectByIsbn(map);
        if (list!=null && list.size()>0){
            return  list.get(0);
        }
        return null;
    }

    @Override
    public int updatePrivacy(Map<String, Object> map) {
      return trUserBookMapper.updatePrivacy(map);

    }

    @Override
    public int save(Map<String, Object> map) {
        return trUserBookMapper.save(map);
    }
}
