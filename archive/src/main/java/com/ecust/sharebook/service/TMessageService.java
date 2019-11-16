package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TMessageService {

    List<MessageInf> list(Map<String, Object> map);


    int insertSelective(MessageInf record);

    int updateByPrimaryKeySelective(MessageInf record);


}
