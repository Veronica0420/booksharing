package com.ecust.sharebook.pojo.util;

import org.apache.catalina.LifecycleState;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;


public class BDself {

    private String isbn;

    private String bookName;

    private String author;

    private String picPath;

    private String briefIntro;

    private List<String> catg;

    private Integer privacy;

    private Integer borrowState;

    private Date addTime;


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }

    public List<String> getCatg() {
        return catg;
    }

    public void setCatg(List<String> catg) {
        this.catg = catg;
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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
