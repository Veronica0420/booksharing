package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.MessageInfMapper;
import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.pojo.util.chat.messageBookId;
import com.ecust.sharebook.service.TBookUserBorrowService;
import com.ecust.sharebook.service.TMessageService;
import com.ecust.sharebook.service.TUserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TMessageServiceImpl implements TMessageService {
    @Autowired
    private MessageInfMapper messageInfMapper;



    @Override
    public List<MessageInf> list(Map<String, Object> map) {
        List<MessageInf> lists = messageInfMapper.list(map);
        if (lists.size() != 0 && lists != null) {
            return lists;
        }
        return null;
    }

    @Override
    public int insertSelective(MessageInf record) {
        return messageInfMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(MessageInf record) {
        return messageInfMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer messageId) {
        return messageInfMapper.deleteByPrimaryKey(messageId);
    }

    @Override
    public int insert(MessageInf record) {
        return messageInfMapper.insert(record);
    }

    @Override
    public MessageInf selectByPrimaryKey(Integer messageId) {
        return messageInfMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public int updateByPrimaryKey(MessageInf record) {
        return messageInfMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<MessageInf> listAll(Map<String, Object> map) {
        return messageInfMapper.listAll(map);
    }

    @Override
    public List<MessageInf> findCurrentBMessage(Map<String, Object> map) {
        return messageInfMapper.findCurrentBMessage(map);
    }

    @Override
    public List<MessageInf> findHistory(Map<String, Object> map) {
        return messageInfMapper.findHistory(map);
    }

    @Override
    public int applyCancelOther(Map<String, Object> map) {
        return messageInfMapper.applyCancelOther(map);
    }

    @Override
    public int cancelReturn(Map<String, Object> map) {
        return messageInfMapper.cancelReturn(map);
    }

    @Override
    public int passReturn(Map<String, Object> map) {
        return messageInfMapper.passReturn(map);
    }

    @Override
    public int rejectApply(Map<String, Object> map) {
        return messageInfMapper.rejectApply(map);
    }

    @Override
    public int passApply(Map<String, Object> map) {
        return messageInfMapper.passApply(map);
    }

    @Override
    public int cancelApply(Map<String, Object> map) {
        return messageInfMapper.cancelApply(map);
    }

    @Override
    public int emptyCountInsertB(Map<String, Object> map) {
        return messageInfMapper.emptyCountInsertB(map) ;
    }

    @Override
    public int emptyCountInsertM(Map<String, Object> map) {
        return messageInfMapper.emptyCountInsertM(map);
    }





}
