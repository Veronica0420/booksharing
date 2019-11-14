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

    @Override
    public List<PostInf> Plist(Map<String, Object> map) {
        List<PostInf> mlist = postInfMapper.Plist(map);
        if(mlist!= null && mlist.size()!=0){
            return mlist;
        }
        return null;
    }

    @Override
    public PostInf post(Map<String, Object> map) {
        return postInfMapper.post(map);
    }

    @Override
    public int deleteByPrimaryKey(Integer postId) {
        return postInfMapper.deleteByPrimaryKey(postId);
    }

    @Override
    public int insert(PostInf record) {
        return postInfMapper.insert(record);
    }

    @Override
    public int insertSelective(PostInf record) {
        return postInfMapper.insertSelective(record);
    }

    @Override
    public PostInf selectByPrimaryKey(Integer postId) {
        return postInfMapper.selectByPrimaryKey(postId);
    }

    @Override
    public int updateByPrimaryKeySelective(PostInf record) {
        return postInfMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PostInf record) {
        return postInfMapper.updateByPrimaryKey(record);
    }
}
