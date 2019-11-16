package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.CommentInfMapper;
import com.ecust.sharebook.pojo.CommentInf;
import com.ecust.sharebook.service.TCommentInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TCommentInfServiceImpl implements TCommentInfService {
    @Autowired
    private CommentInfMapper commentInfMapper ;
    @Override
    public List<CommentInf> list(Map<String, Object> map) {
        List<CommentInf> lists = commentInfMapper.list(map);
        if(lists.size()!=0&&lists!=null){
            return lists;
        }
        return null;

    }

    @Override
    public int insert(CommentInf record) {
        return commentInfMapper.insert( record);
    }
}
