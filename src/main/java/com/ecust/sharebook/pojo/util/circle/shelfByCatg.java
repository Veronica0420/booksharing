package com.ecust.sharebook.pojo.util.circle;

import java.util.Date;

public class shelfByCatg {

    private String isbn;

    private String bookName;

    private String picPath;

    private Date sortTime;

    private String author;

    private String briefIntro;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Date getSortTime() {
        return sortTime;
    }

    public void setSortTime(Date sortTime) {
        this.sortTime = sortTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }


    public  void setAll(String isbn,String bookName,String picPath,String briefIntro){
        this.setPicPath(picPath);
        this.setBookName(bookName);
        this.setIsbn(isbn);
        this.setBriefIntro(briefIntro);

    }
}
