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

    private Boolean deleteBook;


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

    public Boolean getDeleteBook() {
        return deleteBook;
    }

    public void setDeleteBook(Boolean deleteBook) {
        this.deleteBook = deleteBook;
    }

}