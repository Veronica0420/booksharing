package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.service.TBookUserBorrowService;
import com.ecust.sharebook.service.TMessageService;
import com.ecust.sharebook.service.TUserBookService;
import com.ecust.sharebook.service.optService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class optServiceImpl implements optService {


    @Autowired
    private TBookUserBorrowService tBookUserBorrowService;

    @Autowired
    private TUserBookService tUserBookService;

    @Autowired
    private TMessageService tMessageService;


    /***
     * 同意/拒绝
     *
     *  通过申请：USR_BORROW_STATE----2，借阅中，BORROW_RES----1，同意,BORROW_STATE---2,已借出
     *       拒绝6，2 2
     *
     *  确认归还：USR_BORROW_STATE----4，已还书，BORROW_RES----1，同意，BORROW_STATE---0，无申请
     *
     *
     *
     * @param  mode:apply/return;
     *         type:pass,reject
     *
     * */
    @Override
    public Boolean updateByOpt(Integer messageId, Boolean mode, Boolean type, Date date) {

        Map<String, Object> param = new HashMap<>();
        Boolean result = false;
        param.put("messageId", messageId);
        MessageInf messageInfTemp = tMessageService.list(param).get(0);
        param.put("borrowId", messageInfTemp.getmBorrowId());
        rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.list(param).get(0);
        param.put("bookId",rBookUserBorrowTemp.getBookId());

        if (mode) {
            //申请
            List<MessageInf> messageInfList = tUserBookService.listByState1(param);
            if (type) {
                int index = 0;
                for (index = 0; index < messageInfList.size(); index++) {
                    MessageInf me = messageInfList.get(index);
                    MessageInf messageInfTemp2 = new MessageInf();
                    rBookUserBorrow rBookUserBorrowTemp2 = new rBookUserBorrow();
                    rUserBook rUserBookTemp2 = new rUserBook();
                    if (me.getMessageId().equals(messageId)) {
                        messageInfTemp2.setMessageId(messageInfTemp.getMessageId());
                        messageInfTemp2.setBorrowRes(1); //同意
                        rBookUserBorrowTemp2.setBorrowId(messageInfTemp.getmBorrowId());
                        rBookUserBorrowTemp2.setUsrBorrowState(2); //借阅中
                        rBookUserBorrowTemp2.setBorrowDateTime(date);
                        rUserBookTemp2.setBookId(rBookUserBorrowTemp.getBookId());
                        rUserBookTemp2.setBorrowState(2); //已借出
                        Boolean resultTemp = update3(rBookUserBorrowTemp2,rUserBookTemp2,messageInfTemp2);
                        if (resultTemp) {
                            continue;
                        } else {
                            break;
                        }

                    }
                    messageInfTemp2.setMessageId(me.getMessageId());
                    messageInfTemp2.setBorrowRes(2); //拒绝
                    rBookUserBorrowTemp2.setBorrowId(me.getmBorrowId());
                    rBookUserBorrowTemp2.setUsrBorrowState(6); //申请不通过
                    Boolean resultTemp2 = update2(rBookUserBorrowTemp2,messageInfTemp2);

                    if (resultTemp2) {
                        continue;
                    } else {
                        break;
                    }
                }
                if (index == messageInfList.size()) {
                    result = true;
                    return result;
                }
            } else {
                //reject 注：无申请状态
                MessageInf messageInfTemp2 = new MessageInf();
                rBookUserBorrow rBookUserBorrowTemp2 = new rBookUserBorrow();
                rUserBook rUserBookTemp2 = new rUserBook();

                messageInfTemp2.setMessageId(messageInfTemp.getMessageId());
                messageInfTemp2.setBorrowRes(2); //拒绝
                rBookUserBorrowTemp2.setBorrowId(messageInfTemp.getmBorrowId());
                rBookUserBorrowTemp2.setUsrBorrowState(6);  //申请不通过
                Boolean resultTemp2 = update2(rBookUserBorrowTemp2,messageInfTemp2);

                if (messageInfList.size() == 1) {

                    rUserBookTemp2.setBookId(rBookUserBorrowTemp.getBookId());
                    rUserBookTemp2.setBorrowState(0);//无申请

                    int z = tUserBookService.updateByPrimaryKeySelective(rUserBookTemp2);

                    if (resultTemp2 && z == 1) {
                        result = true;
                        return result;
                    }
                } else {
                    if (resultTemp2) {
                        result = true;
                        return result;
                    }
                }
            }
        } else {
            //归还
            MessageInf messageInfTemp2 = new MessageInf();
            rBookUserBorrow rBookUserBorrowTemp2 = new rBookUserBorrow();
            rUserBook rUserBookTemp2 = new rUserBook();
            messageInfTemp2.setMessageId(messageInfTemp.getMessageId());

            rBookUserBorrowTemp2.setBorrowId(messageInfTemp.getmBorrowId());
            if (type) {
                messageInfTemp2.setBorrowRes(1); //同意
                rBookUserBorrowTemp2.setUsrBorrowState(4); //已还书
            } else {
                messageInfTemp2.setBorrowRes(2); //同意
                rBookUserBorrowTemp2.setUsrBorrowState(7); //归还失败
            }
            rBookUserBorrowTemp2.setReturnDateTime(date);
            rUserBookTemp2.setBookId(rBookUserBorrowTemp.getBookId());
            rUserBookTemp2.setBorrowState(0);//无申请

            Boolean resultTemp = update3(rBookUserBorrowTemp2,rUserBookTemp2,messageInfTemp2);
            if (resultTemp) {
                result = true;
                return result;
            }

        }
        return result;
    }


    /**
     * 申请
     **/
    @Transactional
    @Override
    public Boolean appplyBook(rBookUserBorrow rBookUserBorrowTemp, rUserBook rUserBookTemp, MessageInf messageInfTemp) {


        Boolean result = false;

        //insert  rBookUserBorrow
        int i = tBookUserBorrowService.insertSelective(rBookUserBorrowTemp);

        //update  rUserBook
        int j = tUserBookService.updateByPrimaryKeySelective(rUserBookTemp);

        //insert  MessageInf
        messageInfTemp.setmBorrowId(rBookUserBorrowTemp.getBorrowId());
        int m = tMessageService.insertSelective(messageInfTemp);

        if (i == 1 && j == 1 && m == 1) {
            result = true;
        } else
            result = false;

        return result;
    }


    /**
     * 归还
     **/
    @Override
    public Boolean returnBook(rBookUserBorrow rBookUserBorrows, MessageInf message) {
        //update
        int i = tBookUserBorrowService.updateByPrimaryKeySelective(rBookUserBorrows);

        //insert
        int j = tMessageService.insertSelective(message);

        Boolean result = false;
        if (i == 1 && j == 1) {
            result = true;

        } else
            result = false;

        return result;

    }


    /**
     * 取消申请
     **/

    @Override
    public Boolean cancelApply(rBookUserBorrow rBookUserBorrows) {
        //update
        int i = tBookUserBorrowService.updateByPrimaryKeySelective(rBookUserBorrows);
        Boolean result = false;

        rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.selectByPrimaryKey(rBookUserBorrows.getBorrowId());

        rBookUserBorrow rBookUserBorrowTemp2 = new rBookUserBorrow();
        rBookUserBorrowTemp2.setBookId(rBookUserBorrowTemp.getBookId());
        rBookUserBorrowTemp2.setUsrBorrowState(1);//申请

        Boolean isEmpty = checkApplyEmpty(rBookUserBorrowTemp2);

        if (!isEmpty) {
            //若无申请，修改 ruserbook state --0  可借阅
            rUserBook rUserBooks = new rUserBook();
            rUserBooks.setBookId(rBookUserBorrowTemp.getBookId());
            rUserBooks.setBorrowState(Integer.valueOf(0)); //无申请
            int j = tUserBookService.updateByPrimaryKeySelective(rUserBooks);
            if (i == 1 && j == 1) {
                result = true;
                return result;
            }
        }

        if (i == 1) {
            result = true;
        }


        return result;


    }


    /**
     * 申请状态是否存在
     **/
    @Override
    public Boolean checkApplyEmpty(rBookUserBorrow rBookUserBorrows) {
        Map<String, Object> param = new HashMap<>();

        param.put("bookId", rBookUserBorrows.getBookId());
        param.put("userBorrowState", rBookUserBorrows.getUsrBorrowState());
        List<rBookUserBorrow> list = tBookUserBorrowService.list(param);

        Boolean result = false;

        if (list.size() != 0 && list != null) {
            result = true;
        }

        return result;


    }

    @Override
    public Boolean update3(rBookUserBorrow rBookUserBorrows, rUserBook rUserBooks, MessageInf message) {

        Boolean result = false;
        int i = tMessageService.updateByPrimaryKeySelective(message);
        int j = tBookUserBorrowService.updateByPrimaryKeySelective(rBookUserBorrows);
        int z = tUserBookService.updateByPrimaryKeySelective(rUserBooks);
        if (i == 1 && j == 1 && z == 1) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean update2(rBookUserBorrow rBookUserBorrows, MessageInf message) {
        Boolean result = false;
        int j = tBookUserBorrowService.updateByPrimaryKeySelective(rBookUserBorrows);
        int i = tMessageService.updateByPrimaryKeySelective(message);
        if (i == 1 && j == 1) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


}
