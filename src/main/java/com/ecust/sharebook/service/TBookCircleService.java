package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.BookCircleInf;

import java.util.List;
import java.util.Map;


public interface TBookCircleService {
    int insert(Map<String, Object> bcInf);
    List<BookCircleInf> selectLikBCName(Map<String, Object> param);
    List<BookCircleInf> selectbyCreaterID(Map<String, Object> params);
    List<BookCircleInf> selectbyNotCreaterIDMemberID(Map<String, Object> params);
}
