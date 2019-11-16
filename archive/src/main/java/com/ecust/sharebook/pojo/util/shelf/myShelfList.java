package com.ecust.sharebook.pojo.util.shelf;

import java.util.Date;

public class myShelfList implements Comparable<myShelfList> {

    /** USRBOOK**/
    private Date addTime;

    private Integer bookId;

    private Integer ownerId;

    private Date borrowTime;

    /** BORROW**/
    private Integer borrowId;

    private Integer userId;

    /** 自定义**/
    private Date sortTime;

    private  Boolean is_Borrow;


    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }


    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }


    public int compareTo(myShelfList myShelfListTemp)
    {
        if (sortTime.before(myShelfListTemp.sortTime) )
            return -1;
        else if (this.sortTime.after(myShelfListTemp.sortTime) )  //最近
            return 1;
        else
            return 0;

    }


    public Date getSortTime() {
        return sortTime;
    }

    public void setSortTime(Date sortTime) {
        this.sortTime = sortTime;
    }


    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    /**
     * myShelfList,先比较hashcode，一致的场合再比较每个属性的值
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof myShelfList) {
            myShelfList vo = (myShelfList) obj;

            // 比较每个属性的值 一致时才返回true
            if (vo.bookId.equals(this.bookId))
                return true;
        }
        return false;
    }

    /**
     * 重写hashcode 方法，返回的hashCode不一样才再去比较每个属性的值
     */
    @Override
    public int hashCode() {
        return bookId.hashCode() ;
    }


    public Boolean getIs_Borrow() {
        return is_Borrow;
    }

    public void setIs_Borrow(Boolean is_Borrow) {
        this.is_Borrow = is_Borrow;
    }
}
