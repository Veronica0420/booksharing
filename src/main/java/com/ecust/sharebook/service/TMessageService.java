package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;

import java.util.List;
import java.util.Map;

public interface TMessageService {

    List<MessageInf> list(Map<String, Object> map);


    int insertSelective(MessageInf record);
}
