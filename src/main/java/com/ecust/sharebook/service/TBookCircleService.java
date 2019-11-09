package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.BookCircleInf;
import com.ecust.sharebook.pojo.vBookCreaterBcircle;

import java.util.List;
import java.util.Map;


public interface TBookCircleService {
    int insert(BookCircleInf record);
    BookCircleInf selectByPrimaryKey(Integer circleId);
    List<BookCircleInf> selectLikBCName(Map<String, Object> param);
    List<BookCircleInf> selectfromMemberID(Map<String, Object> params);
    List<BookCircleInf> selectbyNotMember(Map<String, Object> params);
    List<BookCircleInf> seletbybName_bc(Map<String, Object> params);
}
