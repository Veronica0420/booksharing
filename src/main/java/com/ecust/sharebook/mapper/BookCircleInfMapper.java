package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.BookCircleInf;

import java.util.List;
import java.util.Map;

public interface BookCircleInfMapper {
    int deleteByPrimaryKey(Integer bookCircleId);

    int insert(BookCircleInf record);

    int insertSelective(BookCircleInf record);

    BookCircleInf selectByPrimaryKey(Integer bookCircleId);

    int updateByPrimaryKeySelective(BookCircleInf record);

    int updateByPrimaryKey(BookCircleInf record);

    int updatePicPath(BookCircleInf record);

    //根据图书圈名查找相似结果  ——Sijar
    List<BookCircleInf> selectLikebcName(Map<String ,Object> map);

    //根据一定条件查出对应的所有图书圈列表   ——Sijar
    List<BookCircleInf> listBycreaterID(Map<String, Object> map);
    //根据一定条件查出对应的未创建的所有图书圈列表   ——Sijar
    List<BookCircleInf> listByNotcreaterID(Map<String, Object> map);
}