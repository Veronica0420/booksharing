package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.vBookCreaterBcircle;

import java.util.List;
import java.util.Map;

public interface vBookMemberBcircleMapper {

    int deleteByPrimaryKey(Map<String, Object> map);

    List<vBookCreaterBcircle> getBaseResultKey(Map<String, Object> map);

    vBookCreaterBcircle selectByPrimaryKey(Map<String, Object> map);

    //根据书名查找有此书的图书圈  ——Sijar
    List<Integer> selectByBookName(Map<String, Object> map);
}
