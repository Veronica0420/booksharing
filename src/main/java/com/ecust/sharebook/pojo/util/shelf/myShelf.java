package com.ecust.sharebook.pojo.util.shelf;

import java.util.*;

/**
 * @author 徐倩倩
 *
 * 功能：shelf 界面加载所需数据封装
 * **/

public class myShelf {

    private String isbn;

    private String bookName;

    private String picPath;

    private Date sortTime;

    private String author;

    private List<myShelfList> myShelfLists;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    /**
     * isBorrow true 借书 用申请借书的时间来排序，false 我的书籍，用上架时间排序
     * **/
    public void sortTime(Integer myId){
        List<myShelfList> myShelfListsTemp = this.getMyShelfLists();

        List<myShelfList> myShelfListsTemp2 = new ArrayList<>();
        Set<myShelfList> set = new TreeSet<myShelfList>();

        for(myShelfList is:myShelfListsTemp){
            if(is.getOwnerId().equals(myId)){
                is.setIs_Borrow(false);
                is.setSortTime(is.getAddTime());
            }else{
                is.setIs_Borrow(true);
                is.setSortTime(is.getBorrowTime());
            }
        }
        set.addAll(myShelfListsTemp);
        myShelfListsTemp2.addAll(set);
        this.setMyShelfLists(myShelfListsTemp2);
        Collections.sort(this.getMyShelfLists());
        this.setSortTime(myShelfListsTemp.get(myShelfListsTemp.size()-1).getSortTime());



    }


    public void setBorrowAll(){
        List<myShelfList> myShelfListsTemp = this.getMyShelfLists();
        for(myShelfList is:myShelfListsTemp){
            is.setIs_Borrow(false);
            is.setSortTime(is.getAddTime());
        }
        Collections.sort(this.getMyShelfLists());
        this.setSortTime(myShelfListsTemp.get(myShelfListsTemp.size()-1).getSortTime());


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

    public List<myShelfList> getMyShelfLists() {
        return myShelfLists;
    }

    public void setMyShelfLists(List<myShelfList> myShelfLists) {
        this.myShelfLists = myShelfLists;
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
}
