package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.vBookCreaterBcircle;
import com.ecust.sharebook.pojo.vBookMemberBcircle;

import java.util.List;
import java.util.Map;

public interface vBookMemberBcircleMapper {

    int deleteByPrimaryKey(Map<String, Object> map);

    List<vBookMemberBcircle> getBaseResultKey(Map<String, Object> map);

    vBookCreaterBcircle selectByPrimaryKey(Map<String, Object> map);

    //根据书名查找有此书（属性公开）的图书圈  ——Sijar
    List<Integer> selectByBookName(Map<String, Object> map);
    //根据图书圈名查找书圈创建者的所有公开书籍 ——Sijar
    List<vBookMemberBcircle> selectByBCId(Map<String, Object> map);
}
