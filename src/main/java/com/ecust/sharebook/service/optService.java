package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
import com.ecust.sharebook.pojo.rUserBook;

import java.util.Date;
import java.util.Map;

public interface optService {



    Boolean updateByOpt(Integer messageId ,Boolean mode, Boolean type, Date date);


    Boolean   appplyBook(rBookUserBorrow rBookUserBorrows, rUserBook rUserBooks, MessageInf message);


    Boolean   returnBook(rBookUserBorrow rBookUserBorrows ,MessageInf message);

    Boolean  cancelApply(rBookUserBorrow rBookUserBorrows);


    Boolean checkApplyEmpty(rBookUserBorrow rBookUserBorrows);


    Boolean update3(rBookUserBorrow rBookUserBorrows, rUserBook rUserBooks, MessageInf message);


    Boolean update2(rBookUserBorrow rBookUserBorrows,  MessageInf message);




}
