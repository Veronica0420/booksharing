package com.ecust.sharebook.pojo;

import java.util.Date;
import java.util.List;

public class rUserBook {
    private Integer bookId;

    private String isbn;

    private Integer ownerId;

    private Integer privacy;

    private Integer borrowState;

    private Date addTime;

    private Integer deleteBook;

    private String  privacyS;

    private String borrowStateS;

    private String time;



    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public Integer getBorrowState() {
        return borrowState;
    }

    public void setBorrowState(Integer borrowState) {
        this.borrowState = borrowState;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getDeleteBook() {
        return deleteBook;
    }

    public void setDeleteBook(Integer deleteBook) {
        this.deleteBook = deleteBook;
    }

    public String getPrivacyS() {
        return privacyS;
    }

    public void setPrivacyS(String privacyS) {
        this.privacyS = privacyS;
    }

    public String getBorrowStateS() {
        return borrowStateS;
    }

    public void setBorrowStateS(String borrowStateS) {
        this.borrowStateS = borrowStateS;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}