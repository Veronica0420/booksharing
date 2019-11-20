package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;

import java.text.ParseException;
import java.util.Date;


public interface optService {



    Boolean updateByOpt(Integer messageId ,Boolean mode, Boolean type, Date date);


    Boolean   appplyBook(rBookUserBorrow rBookUserBorrows, rUserBook rUserBooks, MessageInf message);


    Boolean   returnBook(rBookUserBorrow rBookUserBorrows ,MessageInf message);

    Boolean  cancelApply(rBookUserBorrow rBookUserBorrows);


    Boolean checkApplyEmpty(rBookUserBorrow rBookUserBorrows);


    Boolean update3(rBookUserBorrow rBookUserBorrows, rUserBook rUserBooks, MessageInf message);


    Boolean update2(rBookUserBorrow rBookUserBorrows,  MessageInf message);


    Boolean   appplyBook1(String  muserId, String mfid, String mbookId, String mdate) throws ParseException;

    /***
     *
     * 申请借阅-同意
     * @param messageId
     * @param date
     * @param bookId
     * @return
     */
   Boolean applyAgree(Integer messageId,Date date,Integer bookId);

    /**
     *
     * 申请借阅
     *
     * @param rBookUserBorrowTemp
     * @param ownerId
     * @return
     */


    Boolean applyBook(rBookUserBorrow rBookUserBorrowTemp,Integer ownerId);

    /***
     * 拒绝申请
     *
     * @param messageId
     * @return
     */

    Boolean rejectApply(Integer messageId);

    /**
     *
     * 归还书籍
     * @param messageInf
     * @return
     */
    Boolean returnBook(MessageInf messageInf);

    /**
     * 归还书籍-确认
     * @param messageId
     * @param date
     * @return
     */
    Boolean returnAgree(Integer messageId,Date date);

    /**
     * 归还书籍-拒绝
     * @param messageId
     * @return
     */
    Boolean rejectReturn(Integer messageId,Date date);

    /**
     *
     * 取消申请-主动发起
     * @param borrowId
     * @return
     */
    Boolean cancelApply(Integer borrowId);


Boolean opt(MessageInf messageInf);






}
