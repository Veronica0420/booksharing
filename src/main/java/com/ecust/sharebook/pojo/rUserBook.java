package com.ecust.sharebook.pojo;

import java.util.Date;

public class rUserBook {
    private Integer bookId;

    private String isbn;

    private Integer ownerId;

    private Boolean privacy;

    private Boolean borrowState;

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

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    public Boolean getBorrowState() {
        return borrowState;
    }

    public void setBorrowState(Boolean borrowState) {
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