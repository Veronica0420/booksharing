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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        param.put("bookId", rBookUserBorrowTemp.getBookId());

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
                        Boolean resultTemp = update3(rBookUserBorrowTemp2, rUserBookTemp2, messageInfTemp2);
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
                    Boolean resultTemp2 = update2(rBookUserBorrowTemp2, messageInfTemp2);

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
                Boolean resultTemp2 = update2(rBookUserBorrowTemp2, messageInfTemp2);

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

            Boolean resultTemp = update3(rBookUserBorrowTemp2, rUserBookTemp2, messageInfTemp2);
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

    @Override
    public Boolean appplyBook1(String muserId, String mfid, String mbookId, String mdate) throws ParseException {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(mdate);
        Integer userId = Integer.valueOf(mbookId);
        Integer fid = Integer.valueOf(mfid);
        Integer bookId = Integer.valueOf(mbookId);

        //insert rBookUserBorrow
        rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
        rBookUserBorrows.setBookId(bookId);
        rBookUserBorrows.setBorrowTime(date);
        rBookUserBorrows.setUserId(userId);
        //update rUserBook
        rUserBook rUserBooks = new rUserBook();
        rUserBooks.setBookId(bookId);
        rUserBooks.setBorrowState(1); //待处理

        //insert message
        MessageInf message = new MessageInf();
        message.setmType(0);//申请
        message.setSenderId(userId);  //我
        message.setReceiverId(fid);
        message.setDateTime(date);

        Boolean result = appplyBook(rBookUserBorrows, rUserBooks, message);
        return result;
    }


    /*************************************
     * 申请借阅：
     * 同意：
     * message:
     *      BORROW_RES 1
     *
     * borrow:
     *      USR_BORROW_STATE 2
     *      BORROW_DATE_TIME
     * userBook:
     *      BORROW_STATE 2
     *
     * 其他所有书籍：拒绝
     *      * message:
     *      *        BORROW_RES 2
     *      * borrow:
     *      *      USR_BORROW_STATE 6
     **************************************/


    @Override
    public Boolean applyAgree(Integer messageId, Date date, Integer bookId) {
        Map<String, Object> param = new HashMap<>();
        param.put("messageId", messageId);
        param.put("date", date);
        int i = tMessageService.passApply(param);
        param.clear();
        param.put("bookId", bookId);
        param.put("messageId", messageId);
        int j = tMessageService.applyCancelOther(param);
        if (i == i && j != 0) {
            return true;
        }
        return false;
    }


    /*************************************
     * 申请拒绝；
     * message:
     *        BORROW_RES 2
     * borrow:
     *      USR_BORROW_STATE 6
     * userBook:
     *      判断：BORROW_STATE
     *      有申请：1
     *      无申请：0
     * *************************************/

    @Override
    public Boolean rejectApply(Integer messageId) {
        Map<String, Object> param = new HashMap<>();
        int total = 0;

        param.put("messageId", messageId);
        int i = tMessageService.rejectApply(param);
        if (i != 0) {
            try {
                total = tBookUserBorrowService.emptyCountM(param);
                System.out.println("total" + total);
                if (total == 0) {
                    int z = tMessageService.emptyCountInsertM(param);
                    if (z != 0) {
                        return true;
                    }
                } else {
                    return true;

                }

            } catch (Exception e) {
                e.printStackTrace();
                //无申请
                int z = tMessageService.emptyCountInsertM(param);
                if (z != 0) {
                    return true;
                }

            }

        }
        return false;
    }


    /*************************************
     * 申请借阅；
     *
     * userBook:
     *    BORROW_STATE 1
     *
     * insert:message，borrow
     * *************************************/

    @Override
    public Boolean applyBook(rBookUserBorrow rBookUserBorrowTemp, Integer ownerId) {

        try {
            int i = tBookUserBorrowService.insertSelective(rBookUserBorrowTemp);

            MessageInf messageInfTemp = new MessageInf();
            messageInfTemp.setmType(0);
            messageInfTemp.setmBorrowId(rBookUserBorrowTemp.getBorrowId());
            messageInfTemp.setDateTime(rBookUserBorrowTemp.getBorrowTime());
            messageInfTemp.setSenderId(rBookUserBorrowTemp.getUserId());
            messageInfTemp.setReceiverId(ownerId);
            int j = tMessageService.insertSelective(messageInfTemp);
            Map<String, Object> param = new HashMap<>();
            param.put("bookId", rBookUserBorrowTemp.getBookId());
            int z = tUserBookService.applyopt(param);

            System.out.println("i==" + i + "j==" + j + "z==" + z);
            if (i != 0 && j != 0 && z != 0) {
                return true;
            } else {
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    /*************************************
     * 归还
     * insert:message
     * borrow:
     *      USR_BORROW_STATE 3
     * *************************************/
    @Override
    public Boolean returnBook(MessageInf messageInf) {
        int i = tMessageService.insertSelective(messageInf);
        Map<String, Object> param = new HashMap<>();
        param.put("borrowId", messageInf.getmBorrowId());
        int j = tBookUserBorrowService.returnopt(param);
        if (i != 0 && j != 0) {
            return true;
        } else {
            return false;
        }
    }


    /*************************************
     * 归还：
     * 同意：
     * message:
     *      BORROW_RES 1
     *
     * borrow:
     *      USR_BORROW_STATE 4
     *     RETURN_DATE_TIME
     * userBook:
     *      BORROW_STATE 0
     *
     **************************************/

    @Override
    public Boolean returnAgree(Integer messageId, Date date) {
        Map<String, Object> param = new HashMap<>();
        param.put("messageId", messageId);
        param.put("date", date);
        int i = tMessageService.passReturn(param);
        if (i != 0) {
            return true;
        } else {
            return false;
        }

    }


    /*************************************
     * 归还：
     * 不同意：
     * message:
     *      BORROW_RES 2
     *
     * borrow:
     *      USR_BORROW_STATE 7
     *     RETURN_DATE_TIME
     * userBook:
     *      BORROW_STATE 0
     *
     **************************************/
    @Override
    public Boolean rejectReturn(Integer messageId, Date date) {
        Map<String, Object> param = new HashMap<>();
        param.put("messageId", messageId);
        param.put("date", date);
        int i = tMessageService.cancelReturn(param);
        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }

    /*************************************
     * 取消申请--主动发起
     * message:
     *      BORROW_RES 3
     *
     * borrow:
     *      USR_BORROW_STATE 5
     * userBook:
     *      判断：BORROW_STATE
     *      有申请：1
     *      无申请：0
     *
     *
     **************************************/
    @Override
    public Boolean cancelApply(Integer borrowId) {
        Map<String, Object> param = new HashMap<>();
        Integer total = 0;
        param.put("borrowId", borrowId);
        int i = tMessageService.cancelApply(param);
        System.out.println("i" + i);
        System.out.println("borrowId" + borrowId);

        if (i != 0) {
            try {
                total = tBookUserBorrowService.emptyCountB(param);
                System.out.println("total" + total);
                if (total == 0) {
                    int z = tMessageService.emptyCountInsertB(param);
                    if (z != 0) {
                        return true;
                    }
                } else {
                    return true;

                }

            } catch (Exception e) {
                e.printStackTrace();
                //无申请
                int z = tMessageService.emptyCountInsertB(param);
                if (z != 0) {
                    return true;
                }

            }

        }
        return false;

    }

    @Override
    public Boolean opt(MessageInf messageInf) {
        Boolean flag = false;
        Map<String, Object> param = new HashMap<>();
        param.put("messageId", messageInf.getMessageId());
        rBookUserBorrow rBookUserBorrowTemp = tBookUserBorrowService.selectByMessageId(param);
        Integer bookId = rBookUserBorrowTemp.getBookId();
        Integer mType = messageInf.getmType();
        Integer borrowRes = messageInf.getBorrowRes();
        Date date = messageInf.getDateTime();
        Integer messageId = messageInf.getMessageId();

        if (mType == 0) {  //申请提醒
            if (borrowRes == 1) {//同意
                 flag = applyAgree(messageId, date, bookId);



            } else if (borrowRes == 2) { //拒绝
                 flag = rejectApply(messageId);

            }

        }

        if (mType == 1) {  //归还提醒
            if (borrowRes == 1) {//同意
                 flag = returnAgree(messageId, date);


            } else if (borrowRes == 2) { //拒绝
                 flag = rejectReturn(messageId, date);

            }

        }
        return  flag;
    }
}
