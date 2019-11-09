package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.PostInfMapper;
import com.ecust.sharebook.pojo.PostInf;
import com.ecust.sharebook.service.TPostInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TPostInfServiceImpl implements TPostInfService {
    @Autowired
    private PostInfMapper postInfMapper;

    @Override
    public List<PostInf> list(Map<String, Object> map) {
        List<PostInf> mlist = postInfMapper.list(map);
        if(mlist!= null && mlist.size()!=0){
            return mlist;
        }
        return null;
    }
}
