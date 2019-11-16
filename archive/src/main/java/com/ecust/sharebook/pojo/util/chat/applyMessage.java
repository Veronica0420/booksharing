package com.ecust.sharebook.pojo.util.chat;

import com.google.gson.internal.$Gson$Types;

import java.util.List;

public class applyMessage {

    private String bookName;

    private String author;

    private String picPath;

    private List<messageBookId> messageBookIdLists;

    private int type;//0 申请 1 归还

    private int count;


    public applyMessage(String bookName,String author,String picPath,List<messageBookId> messageBookIdLists,
                        int type, int count){
        this.setAuthor(author);
        this.setBookName(bookName);
        this.setMessageBookIdLists(messageBookIdLists);
        this.setPicPath(picPath);
        this.setType(type);
        this.setCount(count);
    }

    public applyMessage(){

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public List<messageBookId> getMessageBookIdLists() {
        return messageBookIdLists;
    }

    public void setMessageBookIdLists(List<messageBookId> messageBookIdLists) {
        this.messageBookIdLists = messageBookIdLists;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
