package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rUserBook;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface rUserBookMapper {
    int deleteByPrimaryKey(Integer bookId);

    int insert(rUserBook record);

    int insertSelective(rUserBook record);

    rUserBook selectByPrimaryKey(Integer bookId);

    int updateByPrimaryKeySelective(rUserBook record);

    int updateByPrimaryKey(rUserBook record);

    //自定义
    List<Map<String, Object>> list(Map<String, Object> map);


    List<rUserBook>SelectByIsbn(Map<String, Object> map);

    int updatePrivacy(Map<String, Object> map);

    int save(Map<String, Object> map);
}