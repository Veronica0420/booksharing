package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.rUserBookMapper;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.pojo.util.shelf.myShelf;
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
    public    List<Map<String, Object>> selectByOwId(Map<String, Object> params) {
        List<Map<String, Object>>  list = trUserBookMapper.list1(params);
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
    public int updateByPrimaryKeySelective(rUserBook record) {
        return trUserBookMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer bookId) {
        return trUserBookMapper.deleteByPrimaryKey(bookId);
    }

    @Override
    public int save(Map<String, Object> map) {
        return trUserBookMapper.save(map);
    }

    @Override
    public rUserBook selectByPrimaryKey(Integer bookId) {
        return trUserBookMapper.selectByPrimaryKey(bookId);
    }

    /**
     * shelf 加载
     * param: userId
     * **/
    @Override
    public List<myShelf> findShelf(Map<String, Object> map) {
        List<myShelf> list = trUserBookMapper.findShelf(map);
        if(list!=null &&list.size()!=0){
            return list;
        }
        return  null;
    }
    /**
     * othershelf 加载
     * param: ownerId
     * **/
    @Override
    public List<myShelf> findOtherShelf(Map<String, Object> map) {
        List<myShelf> list = trUserBookMapper.findOtherShelf(map);
        if(list!=null &&list.size()!=0){
            return list;
        }
        return  null;
    }

    /**
     * shelf-list 加载
     * param: userId,isbn
     * **/
    @Override
    public  List<myShelf>  findShelfCateLog(Map<String, Object> map) {
        List<myShelf> list = trUserBookMapper.findShelfCateLog(map);
        if(list!=null && list.size()!=0){
            return list;

        }
        return  null;
    }

    /**
     * othershelf-list 加载
     * param: ownerId,isbn
     * **/
    @Override
    public List<myShelf> findOtherShelfCateLog(Map<String, Object> map) {
        List<myShelf> list = trUserBookMapper.findOtherShelfCateLog(map);
        if(list!=null && list.size()!=0){
            return list;

        }
        return  null;
    }

    @Override
    public List<rUserBook> list(Map<String, Object> map) {
        List<rUserBook> list = trUserBookMapper.list(map);
        if(list!=null &&list.size()!=0){
            return list;
        }
        return  null;
    }

    @Override
    public List<Integer> listByState(Map<String, Object> map) {
        List<Integer> list = trUserBookMapper.listByState(map);
        if(list!=null &&list.size()!=0){
            return list;
        }
        return  null;
    }
}
