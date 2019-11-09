package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.CommentInf;

import java.util.List;
import java.util.Map;

public interface TCommentInfService {

    List<CommentInf> list(Map<String, Object> map);

    int insert(CommentInf record);

}
