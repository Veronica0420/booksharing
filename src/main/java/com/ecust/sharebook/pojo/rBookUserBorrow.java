package com.ecust.sharebook.pojo;

import java.util.Date;

public class rBookUserBorrow {
    private Integer borrowId;

    private Integer bookId;

    private Integer userId;

    private Integer usrBorrowState;

    private Date borrowDateTime;

    private Date returnDateTime;

    private Date borrowTime;

    private String  usrBorrowStateS;

    private String  borrowTimeS;

    private String  borrowDateTimeS;

    private String  returnDateTimeS;

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUsrBorrowState() {
        return usrBorrowState;
    }

    public void setUsrBorrowState(Integer usrBorrowState) {
        this.usrBorrowState = usrBorrowState;
    }

    public Date getBorrowDateTime() {
        return borrowDateTime;
    }

    public void setBorrowDateTime(Date borrowDateTime) {
        this.borrowDateTime = borrowDateTime;
    }

    public Date getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(Date returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }


    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }


    public String getUsrBorrowStateS() {
        return usrBorrowStateS;
    }

    public void setUsrBorrowStateS(String usrBorrowStateS) {
        this.usrBorrowStateS = usrBorrowStateS;
    }

    public String getBorrowTimeS() {
        return borrowTimeS;
    }

    public void setBorrowTimeS(String borrowTimeS) {
        this.borrowTimeS = borrowTimeS;
    }

    public String getBorrowDateTimeS() {
        return borrowDateTimeS;
    }

    public void setBorrowDateTimeS(String borrowDateTimeS) {
        this.borrowDateTimeS = borrowDateTimeS;
    }

    public String getReturnDateTimeS() {
        return returnDateTimeS;
    }

    public void setReturnDateTimeS(String returnDateTimeS) {
        this.returnDateTimeS = returnDateTimeS;
    }
}