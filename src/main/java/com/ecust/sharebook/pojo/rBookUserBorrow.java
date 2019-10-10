package com.ecust.sharebook.pojo;

import java.util.Date;

public class rBookUserBorrow {
    private Integer borrowId;

    private Integer bookId;

    private Integer userId;

    private Boolean usrBorrowState;

    private Date borrowDateTime;

    private Date returnDateTime;

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

    public Boolean getUsrBorrowState() {
        return usrBorrowState;
    }

    public void setUsrBorrowState(Boolean usrBorrowState) {
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
}